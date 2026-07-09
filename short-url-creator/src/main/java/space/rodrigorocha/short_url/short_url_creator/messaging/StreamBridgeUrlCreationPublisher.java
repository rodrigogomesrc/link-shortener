package space.rodrigorocha.short_url.short_url_creator.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import space.rodrigorocha.short_url.short_url_creator.model.Url;
import space.rodrigorocha.short_url.short_url_creator.service.UrlCreationEventPublisher;

@Component
public class StreamBridgeUrlCreationPublisher implements UrlCreationEventPublisher {

    private final StreamBridge streamBridge;

    public StreamBridgeUrlCreationPublisher(StreamBridge steamBridge) {
        this.streamBridge = steamBridge;
    }

    @Override
    public void publishUrlCreationEvent(Url url) {
        if (url != null) {
            streamBridge.send("url-created-out-0", url);
        }

    }
}
