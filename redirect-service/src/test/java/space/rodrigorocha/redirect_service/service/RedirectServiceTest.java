package space.rodrigorocha.redirect_service.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import space.rodrigorocha.redirect_service.exception.NotFoundException;
import space.rodrigorocha.redirect_service.model.Url;
import space.rodrigorocha.redirect_service.repository.UrlRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedirectServiceTest {

    @Mock
    private UrlRepository urlRepository;
    private RedirectService redirectService;

    @BeforeEach
    void setUp() {
        redirectService = new RedirectService(urlRepository);
    }

    @Test
    void findRedirectUrl_ShouldReturnOriginalUrl_WhenUrlIsFound() throws NotFoundException {
        String shortCode = "google";
        String originalUrl = "https://google.com";
        Url foundUrl = new Url(shortCode, originalUrl, "user@email.com");

        when(urlRepository.findById(shortCode)).thenReturn(Optional.of(foundUrl));

        String result = redirectService.findRedirectUrl(shortCode);
        assertEquals(originalUrl, result);
        verify(urlRepository).findById(shortCode);
    }

    @Test
    void findRedirectUrl_ShouldReturnException_WhenUrlNotFound() {

        String shortCode = "aaaaa";
        when(urlRepository.findById(shortCode)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> redirectService.findRedirectUrl(shortCode));

        assertEquals("Short URL not found: " + shortCode, exception.getMessage());
        verify(urlRepository).findById(shortCode);

    }

}
