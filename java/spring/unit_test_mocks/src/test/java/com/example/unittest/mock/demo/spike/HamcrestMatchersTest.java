package com.example.unittest.mock.demo.spike;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HamcrestMatchersTest {

    @Test
    public void test_matching_list() {
        List<Integer> numbers = Arrays.asList(12,15,45);
        assertThat(numbers, hasSize(3));
        assertThat(numbers, hasItems(12,45));
        assertThat(numbers, everyItem(greaterThan(10)));
        assertThat(numbers, everyItem(lessThan(111)));
    }

    @Test
    public void test_matching_string() {
        final String data = "ABCD";

        assertThat(data, containsString("BC"));
        assertThat(data, startsWith("AB"));
        assertThat(data, endsWith("CD"));
    }
}
