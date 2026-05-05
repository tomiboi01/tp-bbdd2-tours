package unlp.info.bd2.repositories.jpa;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.TourGuideUser;

@Repository
public interface TourGuideUserRepository extends ListCrudRepository<TourGuideUser, Long> {
}