package unlp.info.bd2.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Service;

@Repository
public interface ServiceRepository extends ListCrudRepository<Service, Long> {

	Optional<Service> findByNameAndSupplierId(String name, Long supplierId);

	@Query("select s from Service s join ItemService i on s.id = i.service.id group by s.id order by sum(i.quantity) desc")
	List<Service> findMostDemandedService(Pageable pageable);
}

