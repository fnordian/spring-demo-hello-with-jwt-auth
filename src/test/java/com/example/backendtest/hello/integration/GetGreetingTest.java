package com.example.backendtest.hello.integration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"jwtSigningKey=secret key"})
@AutoConfigureMockMvc
@Slf4j
public class GetGreetingTest {
    @Autowired
    private MockMvc mockMvc;
    private String signingKey = "secret key";
    private String username = "foo";


    @Test
    public void getGreeting() throws Exception {
        mockMvc.perform(get("/hello").header("Authorization", "Bearer " + issueForUser(username, 0, 20)))
                .andExpect(status().isOk()).andExpect(content().string("Hello " + username));
    }

    @Test
    public void getGreetingWithoutTokenFails() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().is(401));
    }

    @Test
    public void getGreetingWithInvalidTokenFails() throws Exception {
        mockMvc.perform(get("/hello").header("Authorization", "Bearer " + issueForUser(username, 0, -1)))
                .andExpect(status().is(401));
    }

    public String issueForUser(String username, long id, int validSeconds) {
        return Jwts.builder()
                .claim("sub", id)
                .claim("username", username)
                .claim("iat", Instant.now().getEpochSecond())
                .claim("exp", Instant.now().plusSeconds(validSeconds).getEpochSecond())
                .signWith(SignatureAlgorithm.HS256, signingKey.getBytes())
                .compact();
    }

}
