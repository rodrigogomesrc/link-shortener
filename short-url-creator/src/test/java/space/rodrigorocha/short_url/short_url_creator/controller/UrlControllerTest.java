package space.rodrigorocha.short_url.short_url_creator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateCustomUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateShortUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.exception.CustomUrlAlreadyExistsException;
import space.rodrigorocha.short_url.short_url_creator.exception.MaxRetriesReachedException;
import space.rodrigorocha.short_url.short_url_creator.service.UrlService;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UrlService urlService;

    @Test
    void createShortUrl_ShouldReturn200AndUrl_WhenSuccessful() throws Exception {
        CreateShortUrlRecord record = new CreateShortUrlRecord("https://original-website.com", "user@email.com");
        when(urlService.createShortUrl(any(CreateShortUrlRecord.class))).thenReturn("AbCdEfG");

        mockMvc.perform(post("/api/urls")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(content().string("AbCdEfG"));
    }

    @Test
    void createShortUrl_ShouldReturn500_WhenMaxRetriesReached() throws Exception {
        CreateShortUrlRecord record = new CreateShortUrlRecord("https://original-website.com", "user@email.com");
        when(urlService.createShortUrl(any(CreateShortUrlRecord.class)))
                .thenThrow(new MaxRetriesReachedException("Not able to generate unique short URL after maximum retries"));

        mockMvc.perform(post("/api/urls")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(jsonPath("$.detail")
                        .value("Not able to generate unique short URL after maximum retries"))
                .andExpect(jsonPath("$.status")
                        .value(500));
    }

    @Test
    void createCustomUrl_ShouldReturn200AndUrl_WhenSuccessful() throws Exception {

        CreateCustomUrlRecord record = new CreateCustomUrlRecord("meu-link", "https://site-original.com", "user@email.com");
        when(urlService.createCustomUrl(any(CreateCustomUrlRecord.class))).thenReturn("meu-link");

        mockMvc.perform(post("/api/urls/custom")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(content().string("meu-link"));
    }

    @Test
    void createCustomUrl_ShouldReturn409_WhenUrlAlreadyExists() throws Exception {

        CreateCustomUrlRecord record = new CreateCustomUrlRecord("meu-link", "https://site-original.com", "user@email.com");
        when(urlService.createCustomUrl(any(CreateCustomUrlRecord.class)))
                .thenThrow(new CustomUrlAlreadyExistsException("Custom URL already exists"));

        mockMvc.perform(post("/api/urls/custom")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(jsonPath("$.detail")
                        .value("Custom URL already exists"))
                .andExpect(jsonPath("$.status")
                        .value(409));
    }
}