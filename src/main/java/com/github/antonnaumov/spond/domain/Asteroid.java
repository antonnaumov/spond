package com.github.antonnaumov.spond.domain;

import java.time.LocalDate;

public record Asteroid(
        LocalDate approachDate,
        String neoID,
        String name,
        double estimatedDiameterMin,
        double estimatedDiameterMax,
        double missDistance) {
}
