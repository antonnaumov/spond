package com.github.antonnaumov.spond;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.antonnaumov.spond.domain.Asteroid;
import com.github.antonnaumov.spond.encoding.AsteroidSerializer;
import com.github.antonnaumov.spond.persistence.AsteroidDAO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Path("/asteroids")
public final class AsteroidsResource {
    @Inject
    NeoWsClient neoWsClient;
    @Inject
    AsteroidDAO asteroidDAO;

    @GET
    @Produces({"application/json"})
    @JsonSerialize(using = AsteroidSerializer.class)
    public Collection<Asteroid> list(@QueryParam("start_date") LocalDate startDate, @QueryParam("end_date") LocalDate endDate) throws Exception {
        // select missing dates between start and end dates.
        final var missingDates = asteroidDAO.findMissingDatesBetweenStartDateAndEndDate(startDate, endDate);
        if (missingDates.isEmpty()) {
            // if no missing date between start and end dates found, the entire period already cached in the database,
            // selecting the data
            return asteroidDAO.findByStartDateAndEndDate(startDate, endDate);
        }

        // if ANY date between start and end dates is missing call the NASA API.
        // the database caching introduced to minimize NASA API calls,
        // since there is only number of calls limited it does not matter if one date is missing or the entire period is new -
        // the API call is required.
        // ergo, no further analysis to understand what are the exact periods not covered required.
        final var asteroids = neoWsClient.asteroids(startDate, endDate);
        // it is possible to store all the data selected as the set of individual insert queries ignoring the unique
        // data violation errors.
        // however, the single batch insert operation is cheaper (resource wise) and faster (lock wise) from database prospective.
        // the problem with single batch operation - the entire operation fails if ANY record violate ANY constraint.
        // another factor to take into account - Java in-memory operation are lighter when any database operation.
        // this is brings one to the natural conclusion filter out missing data only from all the data selected,
        // and insert the missing data only with the single batch insert operation.
        asteroidDAO.batchInsert(Utils.missingDatesOnly(asteroids, missingDates));
        // select required the requested data from the database.
        // considering it would be internal logic behind the database selection
        // (in the particular case limit to the 10 records, but it could be much more) it looks reasonable to
        // select data from the data and do not duplicate the logic for the original data selection.
        return asteroidDAO.findByStartDateAndEndDate(startDate, endDate);
    }

    @GET
    @Path("/max_diameter_yearly")
    @Produces({"application/json"})
    @JsonSerialize(using = AsteroidSerializer.class)
    public Response maxByDiameterYearly(@QueryParam("start_date") Integer year) {
        final var startDate = LocalDate.of(1, 1, year);
        final var endDate = startDate.plus(1, ChronoUnit.YEARS);
        final var asteroids = asteroidDAO.findByStartDateAndEndDate(startDate, endDate);
        if (asteroids.isEmpty()) {
            // this is the one way to tackle missing data problem.
            // the another way is to call the NASA API to fill the database with the missing data,
            // but I am genuine think it would be bad idea:
            // - the API limits the selection by 7 days only, this means it might about 53 API calls to cover all year missing
            // - the 7 days API request took up to 15 seconds to be done, 53 API calls might take up to 13 minutes which is beyond any reasonable timeout limitation
            // - the number of the API calls is limited, and should use wisely to select more important data over the less important
            // all the above looks like a reasonable ground to let the client decide what to do, if the data for the particular year is missing.
            // the NO_CONTENT response code helps the client to understand the error nature and act respectfully.
            return Response.status(Response.Status.NO_CONTENT.getStatusCode(), String.format("There are no data for the year %d selected yet, please fill the database first", year)).build();
        }
        return Response.status(200).entity(asteroidDAO.findMaxByDiameterByStartAndEndDate(startDate, endDate)).build();
    }
}
