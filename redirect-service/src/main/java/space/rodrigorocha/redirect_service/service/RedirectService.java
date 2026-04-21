package space.rodrigorocha.redirect_service.service;

import org.springframework.stereotype.Service;
import space.rodrigorocha.redirect_service.exception.NotFoundException;
import space.rodrigorocha.redirect_service.model.Url;
import space.rodrigorocha.redirect_service.repository.UrlRepository;

import java.util.Optional;

@Service
public class RedirectService {

    private final UrlRepository repository;

    public RedirectService(UrlRepository repository) {
        this.repository = repository;
    }

    public String findRedirectUrl(String shortUrl) throws NotFoundException {
        Optional<Url> url = repository.findById(shortUrl);
        if (url.isEmpty()) {
            throw new NotFoundException("Short URL not found: " + shortUrl);
        }
        return url.get().getRedirectUrl();
    }
}
