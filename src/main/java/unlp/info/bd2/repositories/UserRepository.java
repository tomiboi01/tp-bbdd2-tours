package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.User;
import unlp.repositories.GenericRepository;

@Repository
public class UserRepository extends GenericRepository<User> {

    public UserRepository() {
        super(User.class);
    }
}
