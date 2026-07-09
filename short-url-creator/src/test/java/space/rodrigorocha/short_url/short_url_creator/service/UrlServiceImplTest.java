package space.rodrigorocha.short_url.short_url_creator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateCustomUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateShortUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.exception.CustomUrlAlreadyExistsException;
import space.rodrigorocha.short_url.short_url_creator.exception.MaxRetriesReachedException;
import space.rodrigorocha.short_url.short_url_creator.model.Url;
import space.rodrigorocha.short_url.short_url_creator.repository.UrlRepository;
import space.rodrigorocha.short_url.short_url_creator.service.impl.UrlServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;
    private UrlService urlService;

    @Mock
    private UrlCreationEventPublisher eventPublisher;

    private final int VALID_INSTANCE_ID = 1;

    @BeforeEach
    void setUp() {
        urlService = new UrlServiceImpl(urlRepository, VALID_INSTANCE_ID, eventPublisher);
    }

    @Test
    void createCustomUrl_ShouldSaveAndReturnCustomUrl_WhenItDoesNotExist() throws CustomUrlAlreadyExistsException {
        CreateCustomUrlRecord record = new CreateCustomUrlRecord(
                "https://original-website.com", "custom-link", "user@email.com", null);
        when(urlRepository.existsById("custom-link")).thenReturn(false);

        String result = urlService.createCustomUrl(record);

        assertEquals("custom-link", result);

        ArgumentCaptor<Url> urlCaptor = ArgumentCaptor.forClass(Url.class);
        verify(urlRepository).save(urlCaptor.capture());

        Url savedUrl = urlCaptor.getValue();
        assertEquals("custom-link", savedUrl.getShortUrl());
        assertEquals("https://original-website.com", savedUrl.getRedirectUrl());
    }

    @Test
    void createCustomUrl_ShouldThrowException_WhenCustomUrlAlreadyExists() throws CustomUrlAlreadyExistsException {

        CreateCustomUrlRecord record = new CreateCustomUrlRecord(
                "https://original-website.com","custom-link",  "user@email.com", null);
        when(urlRepository.existsById("custom-link")).thenReturn(true);

        CustomUrlAlreadyExistsException exception = assertThrows(
                CustomUrlAlreadyExistsException.class,
                () -> urlService.createCustomUrl(record)
        );

        assertEquals("Custom URL already exists", exception.getMessage());
        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    void createShortUrl_ShouldGenerateAndSaveUrl_OnFirstAttempt() throws MaxRetriesReachedException {
        CreateShortUrlRecord record = new CreateShortUrlRecord(
                "https://original-website.com", "user@mail.com", null);
        when(urlRepository.existsById(anyString())).thenReturn(false);

        String result = urlService.createShortUrl(record);

        ArgumentCaptor<Url> urlCaptor = ArgumentCaptor.forClass(Url.class);
        verify(urlRepository).save(urlCaptor.capture());

        assertNotNull(result);
        assertEquals(7, result.length());
        assertTrue(result.startsWith("1"));

        Url savedUrl = urlCaptor.getValue();
        assertEquals(savedUrl.getRedirectUrl(), record.url());
        assertEquals(savedUrl.getUserEmail(), record.userEmail());
        assertEquals(savedUrl.getRedirectUrl(), record.url());
    }

    @Test
    void createShortUrl_ShouldRetryAndSave_WhenCollisionsOccur() throws MaxRetriesReachedException {

        CreateShortUrlRecord record = new CreateShortUrlRecord(
                "https://original-website.com", "user@mail.com", null);
        when(urlRepository.existsById(anyString()))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);


        String result = urlService.createShortUrl(record);

        ArgumentCaptor<Url> urlCaptor = ArgumentCaptor.forClass(Url.class);
        verify(urlRepository).save(urlCaptor.capture());

        assertNotNull(result);
        assertEquals(7, result.length());
        assertTrue(result.startsWith("1"));

        Url savedUrl = urlCaptor.getValue();
        assertEquals(savedUrl.getRedirectUrl(), record.url());
        assertEquals(savedUrl.getUserEmail(), record.userEmail());
        assertEquals(savedUrl.getRedirectUrl(), record.url());

        verify(urlRepository, times(3)).existsById(anyString());
        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    void createShortUrl_ShouldThrowException_WhenMaxRetriesAreReached() {

        CreateShortUrlRecord record = new CreateShortUrlRecord(
                "https://original-website.com", "user@mail.com", null);
        when(urlRepository.existsById(anyString())).thenReturn(true);

        MaxRetriesReachedException exception = assertThrows(
                MaxRetriesReachedException.class,
                () -> urlService.createShortUrl(record)
        );

        assertEquals("Not able to generate unique short URL after maximum retries", exception.getMessage());

        verify(urlRepository, times(5)).existsById(anyString());
        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    void createShortUrl_ShouldThrowIllegalStateException_WhenInstanceIdIsInvalid() {

        UrlServiceImpl invalidUrlServiceImpl = new UrlServiceImpl(urlRepository, -1, eventPublisher);
        UrlServiceImpl invalidUrlServiceImpl2 = new UrlServiceImpl(urlRepository, -1, eventPublisher);
        CreateShortUrlRecord record = new CreateShortUrlRecord(
                "https://original-website.com", "user@email.com", null);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> invalidUrlServiceImpl.createShortUrl(record)
        );

        assertEquals("Invalid Instance ID for Base62", exception.getMessage());

        IllegalStateException exception2 = assertThrows(
                IllegalStateException.class,
                () -> invalidUrlServiceImpl2.createShortUrl(record)
        );

        assertEquals("Invalid Instance ID for Base62", exception2.getMessage());

    }

}
