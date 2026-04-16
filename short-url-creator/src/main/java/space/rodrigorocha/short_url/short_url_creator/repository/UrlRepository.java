package space.rodrigorocha.short_url.short_url_creator.repository;

import space.rodrigorocha.short_url.short_url_creator.model.Url;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<Url, String> {

}
