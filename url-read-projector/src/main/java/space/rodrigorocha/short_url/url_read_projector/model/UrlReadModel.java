package space.rodrigorocha.short_url.url_read_projector.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("url")
public class UrlReadModel {

    @Id
    private String shortUrl;

    private String redirectUrl;

    @TimeToLive
    private Long ttlInSeconds;

    public UrlReadModel() {
    }

    public UrlReadModel(String shortUrl, String redirectUrl, Long ttlInSeconds) {
        this.shortUrl = shortUrl;
        this.redirectUrl = redirectUrl;
        this.ttlInSeconds = ttlInSeconds;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Long getTtlInSeconds() {
        return ttlInSeconds;
    }

    public void setTtlInSeconds(Long ttlInSeconds) {
        this.ttlInSeconds = ttlInSeconds;
    }
}