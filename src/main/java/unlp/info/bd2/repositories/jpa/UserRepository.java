package unlp.info.bd2.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.model.Review;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select distinct u from TourGuideUser u join Review r on u.id = r.purchase.user.id where r.rating = 1")
    List<TourGuideUser> findTourGuidesWithRating1();
}