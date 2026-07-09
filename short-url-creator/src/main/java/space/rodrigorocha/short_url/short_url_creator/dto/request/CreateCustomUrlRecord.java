package space.rodrigorocha.short_url.short_url_creator.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateCustomUrlRecord(

    @NotBlank(message = "Url is required")
    @Size(max = 2048, message = "Url must not exceed 2048 characters")
    @Pattern(regexp = "^((https?://)?([\\w-]+\\.)+[\\w-]{2,}(/.*)?)?$", message = "Invalid URL format")
    String url,

    @Size(max = 15, message = "Custom URL must not exceed 15 characters")
    String customUrl,

    @Pattern(regexp = "^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})?$", message = "Invalid email")
    String userEmail,

    @Future(message = "Expiration date must be a future date")
    Instant expiration
    ) {
}
