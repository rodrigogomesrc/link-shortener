package space.rodrigorocha.redirect_service.service.impl;

import org.springframework.stereotype.Service;
import space.rodrigorocha.redirect_service.exception.NotFoundException;
import space.rodrigorocha.redirect_service.repository.UrlRepository;
import space.rodrigorocha.redirect_service.service.RedirectService;


@Service
public class RedirectServiceImpl implements RedirectService {

    private final UrlRepository repository;

    public RedirectServiceImpl(UrlRepository repository) {
        this.repository = repository;
    }

    public String findRedirectUrl(String shortUrl) {
        return repository.findById(shortUrl)
                .orElseThrow(() ->
                        new NotFoundException("Short URL not found: " + shortUrl))
                .getRedirectUrl();
    }
}
