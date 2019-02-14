package com.example.unittest.mock.demo.spike;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertJTest {

    @Test
    public void test_matching_list() {
        List<Integer> numbers = Arrays.asList(12,15,45);
        assertThat(numbers)
                .hasSize(3)
                .contains(12,15)
                .allMatch(x -> x > 10)
                .allMatch(x -> x < 100);
    }

    @Test
    public void test_matching_string() {
        final String data = "ABCD";

        assertThat("").isEmpty();

        assertThat(data)
                .contains("BC")
                .startsWith("AB")
                .endsWith("CD");
    }
}
