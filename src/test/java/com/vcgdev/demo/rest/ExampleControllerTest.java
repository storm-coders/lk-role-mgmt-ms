package com.vcgdev.demo.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;

@DisplayName("Example Controller Testing")
public class ExampleControllerTest {

    private ExampleRestController controller = new ExampleRestController();
	private HttpEntity<Map<String, String>> securedGreeting;

    @Test
    public void greetingsTest() {
        securedGreeting = controller.securedGreeting();
        assertNotNull(securedGreeting.getBody());

        Map<String, String> greetins = securedGreeting.getBody();
        assertEquals("Hi, from secured endpoint", greetins.get("greeting"));
    }
    
}
