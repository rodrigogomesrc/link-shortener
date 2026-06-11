package space.rodrigorocha.short_url.short_url_creator.service;

import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateCustomUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateShortUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.exception.CustomUrlAlreadyExistsException;
import space.rodrigorocha.short_url.short_url_creator.exception.MaxRetriesReachedException;
import space.rodrigorocha.short_url.short_url_creator.model.Url;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.rodrigorocha.short_url.short_url_creator.repository.UrlRepository;

import java.security.SecureRandom;

@Service
public class UrlService {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UrlRepository urlRepository;
    private final int instanceId;

    public UrlService(UrlRepository urlRepository, @Value("${app.instance.id}") int instanceId) {
        this.urlRepository = urlRepository;
        this.instanceId = instanceId;
    }

    public String createShortUrl(CreateShortUrlRecord record)  {
        return createShortUrl(record.url(), record.userEmail());
    }

    public String createCustomUrl(CreateCustomUrlRecord record)  {
        Url customUrl = new Url(record.customUrl(), record.url(), record.userEmail());
        if (urlRepository.existsById(record.customUrl())) {
           throw new CustomUrlAlreadyExistsException("Custom URL already exists");
        }
        urlRepository.save(customUrl);
        return record.customUrl();
    }

    @NonNull
    private String createShortUrl(String originalUrl, String userEmail) {
        int maxAttempts = 5;
        while (maxAttempts > 0) {
            String shortUrl = this.createRandomString();

            if (!urlRepository.existsById(shortUrl)) {
                Url url = new Url(shortUrl, originalUrl, userEmail);
                urlRepository.save(url);
                return shortUrl;
            }
            maxAttempts--;
        }
        throw new MaxRetriesReachedException("Not able to generate unique short URL after maximum retries");
    }


    private String createRandomString() {
        StringBuilder sb = new StringBuilder(7);

        if (instanceId < 0 || instanceId >= BASE62.length()) {
            throw new IllegalStateException("Invalid Instance ID for Base62");
        }
        sb.append(BASE62.charAt(instanceId));
        for (int i = 0; i < 6; i++) {
            int randomIndex = RANDOM.nextInt(BASE62.length());
            sb.append(BASE62.charAt(randomIndex));
        }
        return sb.toString();
    }


}
