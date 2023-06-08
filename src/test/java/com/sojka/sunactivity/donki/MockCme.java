package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.Cme;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class MockCme {

    private static final String richCmeString;
    private static final Cme richCme;

    static {
        try (InputStream input = MockCme.class.getClassLoader()
                .getResourceAsStream("cme-singleRichCme.json")) {
            Objects.requireNonNull(input);
            richCmeString = new String(input.readAllBytes());
            ObjectMapper mapper = new ObjectMapper();
            richCme = mapper.readValue(richCmeString, Cme.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Cme getRichCme() {
        return richCme;
    }

    public static String getRichCmeString() {
        return richCmeString;
    }
}
