package space.rodrigorocha.short_url.short_url_creator.service;

import space.rodrigorocha.short_url.short_url_creator.model.Url;

public interface UrlCreationEventPublisher {
    void publishUrlCreationEvent(Url url);
}
