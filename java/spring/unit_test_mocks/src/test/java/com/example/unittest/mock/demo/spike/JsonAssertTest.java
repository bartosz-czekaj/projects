package com.example.unittest.mock.demo.spike        ;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class JsonAssertTest {

    final String actualResponse = "{\"id\" : 1,\"name\":\"Ball\",\"price\":10,\"quantity\":100}";

    @Test
    public void jsonAssert_strict_true() throws JSONException {
        final String expectedResponse = "{\"id\" : 1,\"name\":\"Ball\",\"price\":10,\"quantity\":100}";
        JSONAssert.assertEquals(expectedResponse, actualResponse, true);
    }

    @Test
    public void jsonAssert_strict_false() throws JSONException {
        final String expectedResponse = "{\"name\":\"Ball\",\"price\":10}";
        JSONAssert.assertEquals(expectedResponse, actualResponse, false);
    }

    @Test
    public void jsonAssert_without_escape_characters() throws JSONException {
        final String expectedResponse = "{name:Ball, price:10}";
        JSONAssert.assertEquals(expectedResponse, actualResponse, false);
    }
}
