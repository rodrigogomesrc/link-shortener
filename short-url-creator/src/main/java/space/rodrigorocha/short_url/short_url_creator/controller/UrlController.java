package space.rodrigorocha.short_url.short_url_creator.controller;

import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateCustomUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateShortUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.exception.CustomUrlAlreadyExistsException;
import space.rodrigorocha.short_url.short_url_creator.exception.MaxRetriesReachedException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.rodrigorocha.short_url.short_url_creator.service.UrlService;

@Tag(name = "Urls")
@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createShortUrl(@RequestBody CreateShortUrlRecord payload) {
        String url;
        try {
            url = service.createShortUrl(payload);
        } catch (MaxRetriesReachedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not generate short URL");
        }
        return ResponseEntity.ok(url);
    }

    @PostMapping("/custom")
    public ResponseEntity<String> createCustomUrl(@RequestBody CreateCustomUrlRecord payload) {
        String url;
        try {
            url = service.createCustomUrl(payload);
        } catch (CustomUrlAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Custom URL already exists");
        }
        return ResponseEntity.ok(url);
    }

}
