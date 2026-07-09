package space.rodrigorocha.short_url.url_read_projector.service.impl;

import org.springframework.stereotype.Service;
import space.rodrigorocha.short_url.url_read_projector.model.UrlReadModel;
import space.rodrigorocha.short_url.url_read_projector.repository.UrlRepository;
import space.rodrigorocha.short_url.url_read_projector.service.ProjectorService;

@Service
public class ProjectorServiceImpl implements ProjectorService {

    private final UrlRepository repository;

    public ProjectorServiceImpl(UrlRepository repository){
        this.repository = repository;
    }

    @Override
    public void saveProjection(UrlReadModel url) {
        repository.save(url);
    }
}
