package unlp.info.bd2.repositories.jpa;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.User;

@Repository
public interface PurchaseRepository extends ListCrudRepository<Purchase, Long> {

	Optional<Purchase> findByCode(String code);

	List<Purchase> findByUserUsername(String username);

	@Query("select p.user from Purchase p group by p.user having sum(p.totalPrice) > :mount")
	List<User> findUsersSpendingMoreThan(float mount);

	@Query("select count(p) from Purchase p where p.date between :start and :end")
	long countPurchasesBetweenDates(Date start, Date end);

	@Query("select s.service.supplier from Purchase p join p.itemServiceList s group by s.service.supplier order by count(p) desc")
	List<Supplier> findTopSuppliersInPurchases(Pageable pageable);
}
