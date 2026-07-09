package space.rodrigorocha.short_url.url_read_projector.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.rodrigorocha.short_url.url_read_projector.mapper.UrlProjectionMapper;
import space.rodrigorocha.short_url.url_read_projector.model.UrlWriteModel;
import space.rodrigorocha.short_url.url_read_projector.service.ProjectorService;

import java.util.function.Consumer;

@Configuration
public class EventHandler {

    private final UrlProjectionMapper urlProjectionMapper;
    private final ProjectorService projectorService;

    public EventHandler(UrlProjectionMapper urlProjectionMapper, ProjectorService projectorService){
        this.urlProjectionMapper = urlProjectionMapper;
        this.projectorService = projectorService;
    }

    @Bean
    public Consumer<UrlWriteModel> handleUrlCreatedEvent() {
        return event -> {
            if (event == null) {
                return;
            }
            var readModel = urlProjectionMapper.toReadModel(event);
            projectorService.saveProjection(readModel);
        };
    }
}
