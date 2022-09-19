package com.github.antonnaumov.spond.persistence;

import com.github.antonnaumov.spond.domain.Asteroid;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@QuarkusTest
public class AsteroidDAOIT {
    @Inject
    AsteroidDAO dao;

    @Test

    public void testFindByStartAndEndDate() {
        final var asteroids = dao.findByStartDateAndEndDate(LocalDate.of(2015, 9, 7), LocalDate.of(2015, 9, 9));
        Assertions.assertEquals(3, asteroids.size());
        for (final var a : asteroids) {
            if(List.of("2440012", "2465633", "3426410").contains(a.neoID())) {
                continue;
            }
            Assertions.fail("unexpected Neo ID: " + a.neoID());
        }
    }

    @Test
    public void testFindMissingDatesBetweenStartDateAndEndDate() {
        final var missing = dao.findMissingDatesBetweenStartDateAndEndDate(LocalDate.of(2015, 9, 9), LocalDate.of(2015, 9, 11));
        Assertions.assertEquals(2, missing.size());
    }

    @Test
    public void testFindMissingDatesBetweenStartDateAndEndDateEmpty() {
        final var missing = dao.findMissingDatesBetweenStartDateAndEndDate(LocalDate.of(2015, 9, 7), LocalDate.of(2015, 9, 9));
        Assertions.assertTrue(missing.isEmpty());
    }

    @Test
    public void testBatchInsert() {
        final var asteroids = List.of(
                new Asteroid(LocalDate.of(2015, 9, 9), "3117424", "(2002 EC3)", 0.1460679643, 0.3266178974, 36528412.015756619),
                new Asteroid(LocalDate.of(2015, 9, 9), "3727660", "(2015 RW83)", 0.040230458, 0.0899580388, 29790575.427583686)
        );
        dao.batchInsert(asteroids);
        final var stored = dao.findByStartDateAndEndDate(LocalDate.of(2015, 9, 9), LocalDate.of(2015, 9, 10));
        Assertions.assertEquals(3, stored.size());
        for (final var a : stored) {
            if(List.of("2537342", "3117424", "3727660").contains(a.neoID())) {
                continue;
            }
            Assertions.fail("unexpected Neo ID: " + a.neoID());
        }
    }

    @Test
    public void testFindMaxByDiameterByStartAndEndDate() {
        final var max = dao.findMaxByDiameterByStartAndEndDate(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        Assertions.assertNotNull(max);
    }
}
