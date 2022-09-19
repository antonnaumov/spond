package com.github.antonnaumov.spond.encoding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.antonnaumov.spond.domain.Asteroid;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public final class AsteroidSerializer extends JsonSerializer<Asteroid> {
    @Override
    public void serialize(final Asteroid value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", value.name());
        jgen.writeStringField("neo_id", value.neoID());
        jgen.writeStringField("approach_date", value.approachDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        jgen.writeNumberField("estimated_diameter_min_km", value.estimatedDiameterMin());
        jgen.writeNumberField("estimated_diameter_max_km", value.estimatedDiameterMax());
        jgen.writeNumberField("miss_distance", value.missDistance());
        jgen.writeEndObject();
    }
}
