package space.rodrigorocha.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsGatewayFilterFactory extends AbstractGatewayFilterFactory<AnalyticsGatewayFilterFactory.Config> {

    public AnalyticsGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Capture the IP
            String ipAddress = request.getHeaders().getFirst("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddress() != null ?
                        request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
            }

            // User-Agent
            String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
            if (userAgent == null || userAgent.isEmpty()) {
                userAgent = "unknown";
            }

            // Referer
            String referer = request.getHeaders().getFirst(HttpHeaders.REFERER);
            if (referer == null || referer.isEmpty()) {
                referer = "direct";
            }

            // Language
            String acceptLanguage = request.getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE);
            if (acceptLanguage == null || acceptLanguage.isEmpty()) {
                acceptLanguage = "unknown";
            }

            // create the headers
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-Analytics-IP", ipAddress)
                    .header("X-Analytics-Device", userAgent)
                    .header("X-Analytics-Referer", referer)
                    .header("X-Analytics-Language", acceptLanguage)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    public static class Config {

    }
}
