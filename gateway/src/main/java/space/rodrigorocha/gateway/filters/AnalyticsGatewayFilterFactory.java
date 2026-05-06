package space.rodrigorocha.gateway.filters;

import org.jspecify.annotations.NonNull;
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

    public @NonNull GatewayFilter apply(@NonNull Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String ipAddress;
            if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
                ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
            } else {
                ipAddress =  "unknown";
            }

            String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
            if (userAgent == null || userAgent.isEmpty()) userAgent = "unknown";

            String referer = request.getHeaders().getFirst(HttpHeaders.REFERER);
            if (referer == null || referer.isEmpty()) referer = "direct";

            String acceptLanguage = request.getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE);
            if (acceptLanguage == null || acceptLanguage.isEmpty()) acceptLanguage =  "unknown";

            String finalUserAgent = userAgent;
            String finalReferer = referer;
            String finalAcceptLanguage = acceptLanguage;

            ServerHttpRequest mutatedRequest = request.mutate()
                    .headers(httpHeaders -> {
                        httpHeaders.set("X-Analytics-IP", ipAddress);
                        httpHeaders.set("X-Analytics-Device", finalUserAgent);
                        httpHeaders.set("X-Analytics-Referer", finalReferer);
                        httpHeaders.set("X-Analytics-Language", finalAcceptLanguage);
                    })
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    public static class Config {

    }
}
