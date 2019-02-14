package com.example.spring.boot.conf.demo;

import com.example.spring.boot.conf.demo.model.Question;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootConfExampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private HttpHeaders headers =  new HttpHeaders();

    private String createHttpHeadersAuthorizationValue(String username, String password) {
        final String auth = String.format("%s:%s", username, password);
        byte [] encodeAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
        return String.format("Basic %s", new String(encodeAuth));
    }

    @Before
    public void before(){
        headers.add("Authorization", createHttpHeadersAuthorizationValue("user1", "pass"));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testRetrieveSurveyQuestion() throws JSONException {
        final String url = createUrl("/surveys/Survey1/questions/Question1");
        final TestRestTemplate restTemplate = new TestRestTemplate();

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        final String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\",\"correctAnswer\":\"Russia\",\"options\":[\"India\",\"Russia\",\"United States\",\"China\"]}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    @Test
    public void retrieveAllSurveyQuestions() {
        final String url = createUrl("/surveys/Survey1/questions");
        final TestRestTemplate restTemplate = new TestRestTemplate();

        ResponseEntity<List<Question>> response = restTemplate.exchange(url,
                HttpMethod.GET, new HttpEntity<String>("DUMMY_DOESNT_MATTER", headers),
                new ParameterizedTypeReference<List<Question>>() {});

        Question sampleQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        assertEquals(4, response.getBody().size());
        assertTrue(response.getBody().contains(sampleQuestion));
    }

    @Test
    public void createSurveyQuestion() {
        final TestRestTemplate restTemplate = new TestRestTemplate();

        Question question = new Question("DOESN'T MATTER", "Smallest Number",
                "1", Arrays.asList("1", "2", "3", "4"));

        ResponseEntity<String> response = restTemplate.exchange(
                createUrl("/surveys/Survey1/questions/"), HttpMethod.POST,
                new HttpEntity<Question>(question, headers), String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).get(0), containsString("/surveys/Survey1/questions/"));
    }

    private String createUrl(String url) {
        return  String.format("http://localhost:%d%s", port, url);
    }
}
