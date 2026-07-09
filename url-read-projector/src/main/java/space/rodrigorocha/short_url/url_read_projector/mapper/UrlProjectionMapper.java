package space.rodrigorocha.short_url.url_read_projector.mapper;

import org.springframework.stereotype.Component;
import space.rodrigorocha.short_url.url_read_projector.model.UrlReadModel;
import space.rodrigorocha.short_url.url_read_projector.model.UrlWriteModel;

import java.time.Duration;
import java.time.Instant;

@Component
public class UrlProjectionMapper {

    public UrlReadModel toReadModel(UrlWriteModel event) {
        if (event == null) {
            return null;
        }
        Long ttl = calculateTtl(event.getExpiration());
        return new UrlReadModel(
                event.getShortUrl(),
                event.getRedirectUrl(),
                ttl
        );
    }

    private Long calculateTtl(Instant expiration) {
        if (expiration == null) {
            return null;
        }
        long remainingSeconds = Duration.between(Instant.now(), expiration).toSeconds();

        if (remainingSeconds <= 0) {
            return 1L;
        }
        return remainingSeconds;
    }

}
