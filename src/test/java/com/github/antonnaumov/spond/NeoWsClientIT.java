package com.github.antonnaumov.spond;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;

@QuarkusTest
public class NeoWsClientIT {
    @Inject
    NeoWsClient client;

    @Test
    public void testAsteroidsList() throws Exception {
        final var asteroids = client.asteroids(LocalDate.of(2015, 9, 6), LocalDate.of(2015, 9, 8));
        Assertions.assertNotNull(asteroids);
        Assertions.assertFalse(asteroids.isEmpty());
    }
}
