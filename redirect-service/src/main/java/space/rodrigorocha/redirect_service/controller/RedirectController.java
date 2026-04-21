package space.rodrigorocha.redirect_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.rodrigorocha.redirect_service.service.RedirectService;

import java.net.URI;

@RestController
public class RedirectController {

    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code){
        String urlToRedirect;
        try {
            urlToRedirect = redirectService.findRedirectUrl(code);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlToRedirect))
                .build();
    }


}
