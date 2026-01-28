package model;

import jakarta.persistence.*;

@Entity
public class Url {

    @Id
    @Column(name = "short_url", nullable = false, unique = true, length = 15)
    private String shortUrl;

    @Column(name = "redirect_url", nullable = false, length = 2048)
    private String redirectUrl;

    @Column(name = "user_email", nullable = false, length = 255)
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
