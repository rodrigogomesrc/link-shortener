package space.rodrigorocha.gateway.filters;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
public class NotFoundGlobalFilter implements GlobalFilter, Ordered {

    @Value("classpath:/static/error/404.html")
    private Resource notFoundPage;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (getStatusCode() != null && getStatusCode().equals(HttpStatus.NOT_FOUND)) {

                    getHeaders().setContentType(MediaType.TEXT_HTML);
                    getHeaders().setContentLength(notFoundPage.isFile() ? contentLength() : -1);

                    return Flux.from(body)
                            .doOnNext(DataBufferUtils::release)
                            .thenMany(DataBufferUtils.read(notFoundPage, originalResponse.bufferFactory(), 4096))
                            .as(flux -> super.writeWith(flux));
                }

                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                if (getStatusCode() != null && getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    return writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
                return super.writeAndFlushWith(body);
            }

            private long contentLength() {
                try {
                    return notFoundPage.contentLength();
                } catch (Exception e) {
                    return -1;
                }
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }
}