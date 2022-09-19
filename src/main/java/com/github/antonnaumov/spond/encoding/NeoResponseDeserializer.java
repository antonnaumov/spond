package com.github.antonnaumov.spond.encoding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.antonnaumov.spond.domain.Asteroid;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class NeoResponseDeserializer {
    public Collection<Asteroid> deserialize(final InputStream stream) throws IOException {
        final var mapper = new ObjectMapper();
        final var node = mapper.readTree(stream);
        final var objects = node.get("near_earth_objects");
        final var fields = objects.fieldNames();
        final var asteroids = new ArrayList<Asteroid>();
        while (fields.hasNext()) {
            final var field = fields.next();
            final var array = objects.get(field);
            for (var i = 0; i < array.size(); i++) {
                final var value = array.get(i);
                final var approachData = value.get("close_approach_data");
                final var estimatedDiameter = value.get("estimated_diameter").get("kilometers");
                asteroids.add(new Asteroid(
                        LocalDate.parse(
                                ((JsonNode) approachData.get(0).get("close_approach_date")).asText(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        ),
                        ((JsonNode) value.get("neo_reference_id")).asText(),
                        ((JsonNode) value.get("name")).asText(),
                        ((JsonNode) estimatedDiameter.get("estimated_diameter_min")).asDouble(0),
                        ((JsonNode) estimatedDiameter.get("estimated_diameter_max")).asDouble(0),
                        ((JsonNode) approachData.get(0).get("miss_distance").get("kilometers")).asDouble(0)
                ));
            }
        }
        return Collections.unmodifiableList(asteroids);
    }
}