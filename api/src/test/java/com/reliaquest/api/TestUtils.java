package com.reliaquest.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class TestUtils {

    public static JsonNode readJson(String filename) {
        URL resource = TestUtils.class.getClassLoader().getResource(filename);
        try {
            return new ObjectMapper().readTree(resource);
        } catch (IOException ex) {
            return null;
        }
    }
}
