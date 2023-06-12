package com.sojka.sunactivity.donki;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojka.sunactivity.donki.domain.Cme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class MockCme {

    private static final String richCmeString;
    private static final String htmlWithAnimations;
    private static final Cme richCme;

    private MockCme() {
        throw new AssertionError();
    }

    static {
        ClassLoader loader = MockCme.class.getClassLoader();
        try (InputStream input = loader.getResourceAsStream("cme-singleRichCme.json");
        InputStream html = loader.getResourceAsStream("cme-htmlWithAnimations.html")) {
            richCmeString = new String(Objects.requireNonNull(input).readAllBytes());
            ObjectMapper mapper = new ObjectMapper();
            richCme = mapper.readValue(richCmeString, Cme.class);
            htmlWithAnimations = new String(Objects.requireNonNull(html).readAllBytes());
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

    public static String getHtmlWithAnimations() {
        return htmlWithAnimations;
    }
}
