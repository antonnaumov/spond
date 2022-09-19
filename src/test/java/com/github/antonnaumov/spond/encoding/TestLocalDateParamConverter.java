package com.github.antonnaumov.spond.encoding;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TestLocalDateParamConverter {
    private final LocalDateParamConverter converter = new LocalDateParamConverter();

    @Test
    public void testFromString() {
        Assertions.assertEquals(LocalDate.of(2022, 9, 12), converter.fromString("2022-09-12"));
    }

    @Test
    public void testFromStringNull() {
        Assertions.assertNull(converter.fromString(null));
    }

    @Test
    public void testFromStringEmpty() {
        final var e = Assertions.assertThrows(DateTimeParseException.class, () -> converter.fromString(""));
        Assertions.assertEquals("Text '' could not be parsed at index 0", e.getMessage());
    }

    @Test
    public void testFromStringInvalidDate() {
        final var e = Assertions.assertThrows(DateTimeParseException.class, () -> converter.fromString("2022/09/12"));
        Assertions.assertEquals("Text '2022/09/12' could not be parsed at index 4", e.getMessage());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("2022-09-12", converter.toString(LocalDate.of(2022, 9, 12)));
    }

    @Test
    public void testToStringNull() {
        Assertions.assertNull(converter.toString(null));
    }
}
