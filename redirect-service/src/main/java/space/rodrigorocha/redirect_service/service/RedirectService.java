package space.rodrigorocha.redirect_service.service;

import org.springframework.stereotype.Service;
import space.rodrigorocha.redirect_service.exception.NotFoundException;
import space.rodrigorocha.redirect_service.repository.UrlRepository;


@Service
public class RedirectService {

    private final UrlRepository repository;

    public RedirectService(UrlRepository repository) {
        this.repository = repository;
    }

    public String findRedirectUrl(String shortUrl) {
        return repository.findById(shortUrl)
                .orElseThrow(() ->
                        new NotFoundException("Short URL not found: " + shortUrl))
                .getRedirectUrl();
    }
}
