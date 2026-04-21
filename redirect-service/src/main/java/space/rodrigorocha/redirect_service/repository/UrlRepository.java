package space.rodrigorocha.redirect_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.rodrigorocha.redirect_service.model.Url;

import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, String> {

}
