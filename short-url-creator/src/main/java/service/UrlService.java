package service;

import dto.request.CreateCustomUrlRecord;
import dto.request.CreateShortUrlRecord;
import exception.MaxRetriesReachedException;
import model.Url;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import repository.UrlRepository;

import java.security.SecureRandom;

@Service
public class UrlService {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UrlRepository urlRepository;

    @Value("${app.instance.id}")
    private final int instanceId;

    public UrlService(UrlRepository urlRepository, int instanceId) {
        this.urlRepository = urlRepository;
        this.instanceId = instanceId;
    }

    public String createShortUrl(CreateShortUrlRecord record) throws MaxRetriesReachedException {
        return saveUrl(record.url(), record.userEmail());
    }

    public String createCustomUrl(CreateCustomUrlRecord record) throws MaxRetriesReachedException {
        return saveUrl(record.url(), record.userEmail());
    }

    @NonNull
    private String saveUrl(String originalUrl, String newUrl) throws MaxRetriesReachedException {
        int maxAttempts = 5;
        while (maxAttempts > 0) {
            try {
                String shortUrl = this.createRandomString();
                Url url = new Url(shortUrl, originalUrl, newUrl);
                urlRepository.save(url);
                return shortUrl;
            } catch (DataIntegrityViolationException e) {
                maxAttempts--;
            }
        }
        throw new MaxRetriesReachedException("Not able to generate unique short URL after maximum retries");
    }


    private String createRandomString() {
        StringBuilder sb = new StringBuilder(7);

        if (instanceId < 0 || instanceId >= BASE62.length()) {
            throw new IllegalStateException("Instance ID inválido para Base62");
        }
        sb.append(BASE62.charAt(instanceId));
        for (int i = 0; i < 6; i++) {
            int randomIndex = RANDOM.nextInt(BASE62.length());
            sb.append(BASE62.charAt(randomIndex));
        }
        return sb.toString();
    }


}
