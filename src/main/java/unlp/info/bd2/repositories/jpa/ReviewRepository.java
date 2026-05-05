package unlp.info.bd2.repositories.jpa;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Review;

@Repository
public interface ReviewRepository extends ListCrudRepository<Review, Long> {
}