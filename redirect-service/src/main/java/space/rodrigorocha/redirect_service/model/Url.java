package space.rodrigorocha.redirect_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "url")
public class Url implements Serializable {

    @Id
    private String shortUrl;

    private String redirectUrl;

    @Indexed
    private String userEmail;

    public Url() {
    }

    public Url(String shortUrl, String redirectUrl, String userEmail) {
        this.shortUrl = shortUrl;
        this.redirectUrl = redirectUrl;
        this.userEmail = userEmail;
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
}