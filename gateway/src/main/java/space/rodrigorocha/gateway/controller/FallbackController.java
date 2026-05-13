package space.rodrigorocha.gateway.controller;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @Value("classpath:/static/error/503.html")
    private Resource serviceUnavailablePage;

    @RequestMapping
    public ResponseEntity<Resource> globalFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.TEXT_HTML)
                .body(serviceUnavailablePage);
    }
}