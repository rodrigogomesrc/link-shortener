package space.rodrigorocha.short_url.url_read_projector.model;
import java.io.Serializable;
import java.time.Instant;

public class UrlWriteModel implements Serializable {

    private String shortUrl;

    private String redirectUrl;

    private String userEmail;

    private Instant createdAt;

    private Instant expiration;

    public UrlWriteModel() {
    }

    public UrlWriteModel(String shortUrl,
               String redirectUrl,
               String userEmail,
               Instant expiration) {
        this.shortUrl = shortUrl;
        this.redirectUrl = redirectUrl;
        this.userEmail = userEmail;
        this.createdAt = Instant.now();
        this.expiration = expiration;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }
}