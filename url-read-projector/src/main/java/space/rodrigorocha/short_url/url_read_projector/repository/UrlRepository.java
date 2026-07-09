package space.rodrigorocha.short_url.url_read_projector.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.rodrigorocha.short_url.url_read_projector.model.UrlReadModel;

@Repository
public interface UrlRepository extends CrudRepository<UrlReadModel, String> {
}
