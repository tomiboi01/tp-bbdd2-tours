package unlp.info.bd2.repositories.jpa;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Supplier;

@Repository
public interface SupplierRepository extends ListCrudRepository<Supplier, Long> {

    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);
}