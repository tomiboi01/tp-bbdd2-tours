package unlp.info.bd2.repositories.jpa;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Stop;

@Repository
public interface StopRepository extends ListCrudRepository<Stop, Long> {

    List<Stop> findByNameStartingWith(String name);
}