package com.github.antonnaumov.spond;

import com.github.antonnaumov.spond.domain.Asteroid;
import com.github.antonnaumov.spond.persistence.AsteroidEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class Utils {
    private Utils() {}

    public static AsteroidEntity asteroidToEntity(final Asteroid value) {
        final var entity = new AsteroidEntity();
        entity.setApproachDate(value.approachDate());
        entity.setNeoID(value.neoID());
        entity.setName(value.name());
        entity.setEstimatedDiameterMin(value.estimatedDiameterMin());
        entity.setEstimatedDiameterMax(value.estimatedDiameterMax());
        entity.setMissDistance(value.missDistance());
        return entity;
    }

    public static Asteroid entityToAsteroid(final AsteroidEntity value) {
        return new Asteroid(
                value.getApproachDate(),
                value.getNeoID(),
                value.getName(),
                value.getEstimatedDiameterMin(),
                value.getEstimatedDiameterMax(),
                value.getMissDistance()
        );
    }

    public static Collection<Asteroid> missingDatesOnly(final Collection<Asteroid> asteroids, final Collection<LocalDate> missingDates) {
        final var filtered = new ArrayList<Asteroid>();
        for (final var a : asteroids) {
            if (missingDates.contains(a.approachDate())) {
                filtered.add(a);
            }
        }
        return Collections.unmodifiableList(filtered);
    }
}
