package com.junho.systemdesign.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junho.systemdesign.urlshortener.controller.dto.UrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShortenUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void 유효한_요청일_경우_200_반환() throws Exception {
        // given
        UrlRequest urlRequest = new UrlRequest("http://www.example.com");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/data/shorten")
                        .content(mapper.writeValueAsString(urlRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void 유효하지_않은_요청일경우_400_반환() throws Exception {
        // given
        UrlRequest urlRequest = new UrlRequest("empty");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/data/shorten")
                        .content(mapper.writeValueAsString(urlRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
