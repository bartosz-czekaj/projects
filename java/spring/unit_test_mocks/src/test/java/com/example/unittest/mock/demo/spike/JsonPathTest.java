package com.example.unittest.mock.demo.spike;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Condition;
import org.json.JSONException;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonPathTest {
    @Test
    public void test_json() throws JSONException {
        final String responseFromService = "[\n" +
                "{\"id\":2000, \"name\":\"Pensil\",\"quantity\":5}," +
                "{\"id\":2001, \"name\":\"Pen\",\"quantity\":15}," +
                "{\"id\":2002, \"name\":\"Eraser\",\"quantity\":25}," +
                "]";

        DocumentContext parsedContext = JsonPath.parse(responseFromService);

        int length = parsedContext.read("$.length()");
        assertThat(length).isEqualTo(3);

        List<Integer> ids = parsedContext.read("$..id");
        assertThat(ids).containsExactly(2000,2001,2002);

        final LinkedHashMap read1 = parsedContext.read("$.[1]");
        assertThat(read1)
                .containsKeys("id","name","quantity")
                .containsValues(2001,"Pen",15 );


        final JSONArray read2 = parsedContext.read("$.[0:2]");
        assertThat(read2)
                .hasSize(2)
                .isInstanceOf(List.class)
                .asList();

        Condition<LinkedHashMap> penChecker = new Condition<LinkedHashMap>("is a pen"){
            public boolean matches(LinkedHashMap player) {
                return player.containsKey("name") && player.containsValue("Pen");
            };
        };

        assertThat(read2.get(1)).isInstanceOf(LinkedHashMap.class);
        assertThat(((LinkedHashMap) read2.get(1)))
                .containsKey("name")
                .containsValue(15)
                .has(penChecker);

        final JSONArray read3 = parsedContext.read("$.[?(@.name=='Eraser')]");
        assertThat(read3)
                .hasSize(1);

        final JSONArray read4 = parsedContext.read("$.[?(@.quantity>=15)]");
        assertThat(read4)
                .hasSize(2);


    }
}
