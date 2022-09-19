package com.github.antonnaumov.spond.encoding;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNeoResponseDeserializer {
    @Test
    public void testDeserialize() throws Exception {
        try(var file = this.getClass().getClassLoader().getResourceAsStream("neofeed.json")) {
            final var res = new NeoResponseDeserializer().deserialize(file);
            Assertions.assertFalse(res.isEmpty());
        }
    }
}
