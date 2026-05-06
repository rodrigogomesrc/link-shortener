package space.rodrigorocha.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    @Primary
    public RedisRateLimiter defaultRateLimiter() {
        return new RedisRateLimiter(10, 100, 1);
    }

    @Bean
    public RedisRateLimiter writeRateLimiter() {
        return new RedisRateLimiter(1, 10, 1);
    }

    @Bean
    public RedisRateLimiter readRateLimiter() {
        return new RedisRateLimiter(10, 100, 1);
    }

    @Bean
    public KeyResolver userIpKeyResolver() {
        return exchange -> {
            String ip = "unknown";

            if (exchange.getRequest().getRemoteAddress() != null &&
                    exchange.getRequest().getRemoteAddress().getAddress() != null) {
                ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            }

            return Mono.just(ip);
        };
    }
}