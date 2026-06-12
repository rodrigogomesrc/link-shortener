package space.rodrigorocha.short_url.short_url_creator.service;

import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateCustomUrlRecord;
import space.rodrigorocha.short_url.short_url_creator.dto.request.CreateShortUrlRecord;

public interface UrlService {
    public String createShortUrl(CreateShortUrlRecord record);
    public String createCustomUrl(CreateCustomUrlRecord record);
}
