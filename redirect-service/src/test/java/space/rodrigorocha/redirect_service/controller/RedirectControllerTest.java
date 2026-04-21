package space.rodrigorocha.redirect_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import space.rodrigorocha.redirect_service.exception.NotFoundException;
import space.rodrigorocha.redirect_service.service.RedirectService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RedirectController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RedirectService redirectService;

    @Test
    void redirect_ShouldReturn302AndLocationHeader_WhenUrlIsFound() throws Exception {
        String shortCode = "google";
        String originalUrl = "https://google.com";

        when(redirectService.findRedirectUrl(shortCode)).thenReturn(originalUrl);

        mockMvc.perform(get("/" + shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    void redirect_ShouldReturn404_WhenUrlNotFound() throws Exception {
        String shortCode = "aaaaa";

        when(redirectService.findRedirectUrl(shortCode)).thenThrow(new NotFoundException("Short URL not found: " + shortCode));

        mockMvc.perform(get("/" + shortCode))
                .andExpect(status().isNotFound());
    }

}
