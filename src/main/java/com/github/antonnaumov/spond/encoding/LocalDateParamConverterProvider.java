package com.github.antonnaumov.spond.encoding;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Provider
public final class LocalDateParamConverterProvider implements ParamConverterProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        if (rawType.isAssignableFrom(LocalDate.class)) {
            return (ParamConverter<T>) new LocalDateParamConverter();
        }
        return null;
    }
}

final class LocalDateParamConverter implements ParamConverter<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate fromString(final String value) {
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value, formatter);
    }

    @Override
    public String toString(final LocalDate value) {
        if (value == null) {
            return null;
        }
        return value.format(formatter);
    }
}
