package com.github.antonnaumov.spond.encoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.antonnaumov.spond.domain.Asteroid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestAsteroidSerializer {
    private static final String EXPECTED = "{" +
            "\"name\":\"(2015 FC35)\"," +
            "\"neo_id\":\"3713989\"," +
            "\"approach_date\":\"2015-09-07\"," +
            "\"estimated_diameter_min_km\":0.1010543415," +
            "\"estimated_diameter_max_km\":0.2259643771," +
            "\"miss_distance\":4.807702208347585E7" +
            "}";

    @Test
    public void serializeTest() throws Exception {
        final var a = new Asteroid(
                LocalDate.parse("2015-09-07", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                "3713989",
                "(2015 FC35)",
                0.1010543415,
                0.2259643771,
                48077022.083475854
        );
        final var mapper = new ObjectMapper();
        final var module = new SimpleModule();
        module.addSerializer(Asteroid.class, new AsteroidSerializer());
        mapper.registerModule(module);

        final var json = mapper.writeValueAsString(a);
        Assertions.assertEquals(EXPECTED, json);
    }
}