package controller;

import dto.request.CreateCustomUrlRecord;
import dto.request.CreateShortUrlRecord;
import exception.MaxRetriesReachedException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UrlService;

@Tag(name = "Urls")
@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> createShortUrl(@RequestBody CreateShortUrlRecord payload) {
        String url = "";
        try {
            url = service.createShortUrl(payload);
        } catch (MaxRetriesReachedException e) {
            return ResponseEntity.status(500).body("Could not generate short URL");
        }
        return ResponseEntity.ok(url);
    }

    @GetMapping("/custom")
    public ResponseEntity<String> createCustomUrl(@RequestBody CreateCustomUrlRecord payload) {
        String url = "";
        try {
            url = service.createCustomUrl(payload);
        } catch (MaxRetriesReachedException e) {
            return ResponseEntity.status(500).body("Could not generate short URL");
        }
        return ResponseEntity.ok(url);
    }

}
