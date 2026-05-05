package unlp.info.bd2.repositories.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Route;

@Repository
public interface RouteRepository extends ListCrudRepository<Route, Long> {

	List<Route> findByPriceLessThan(float price);

	@Query("select r from Route r join r.stops s where s.id = :stopId")
	List<Route> findRoutesWithStop(Long stopId);

	@Query("select max(size(r.stops)) from Route r")
	Long findMaxStopOfRoutes();

	@Query("select r from Route r where r.id not in (select p.route.id from Purchase p)")
	List<Route> findRoutsNotSell();

	@Query("select r from Route r join Purchase p on r.id = p.route.id join Review rev on p.id = rev.purchase.id group by r.id order by avg(rev.rating) desc")
	List<Route> findTop3RoutesWithMaxRating(Pageable pageable);

}

