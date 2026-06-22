package space.rodrigorocha.short_url.short_url_creator.controller;

import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateCustomUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateShortUrlRecord;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.rodrigorocha.short_url.short_url_creator.service.UrlService;
import space.rodrigorocha.short_url.short_url_creator.service.impl.UrlServiceImpl;

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
        return ResponseEntity.ok(service.createShortUrl(payload));
    }

    @PostMapping("/custom")
    public ResponseEntity<String> createCustomUrl(@RequestBody CreateCustomUrlRecord payload) {
        return ResponseEntity.ok(service.createCustomUrl(payload));
    }

}
