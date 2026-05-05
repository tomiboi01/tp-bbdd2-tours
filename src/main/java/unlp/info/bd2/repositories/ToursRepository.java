package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {
    User createUser(User user) throws ToursException;
    DriverUser createDriverUser(DriverUser driverUser) throws ToursException;
    TourGuideUser createTourGuideUser(TourGuideUser tourGuideUser) throws ToursException;
    Optional<User> getUserById(Long id) throws ToursException;
    Optional<User> getUserByUsername(String username) throws ToursException;
    User updateUser(User user) throws ToursException;
    void deleteUser(User user) throws ToursException;
    Stop createStop(String name, String description) throws ToursException;
    List<Stop> getStopByNameStart(String name);
    Route createRoute(Route route) throws ToursException;
    Optional<Route> getRouteById(Long id);
    List<Route> getRoutesBelowPrice(float price);
    void assignDriverByUsername(String username, Long idRoute) throws ToursException;
    void assignTourGuideByUsername(String username, Long idRoute) throws ToursException;
    Supplier createSupplier(Supplier supplier) throws ToursException;
    Supplier updateSupplier(Supplier supplier) throws ToursException;
    void deleteSupplier(Supplier supplier) throws ToursException;
    Service updateServicePriceById(Long id, float newPrice) throws ToursException;
    Optional<Supplier> getSupplierById(Long id);
    Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber);
    Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException;
    Purchase createPurchase(Purchase purchase) throws ToursException;
    ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException;
    Optional<Purchase> getPurchaseByCode(String code);
    void deletePurchase(Purchase purchase) throws ToursException;
    Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException;

    void deleteRoute(Route route) throws ToursException;
    // CONSULTAS HQL
    List<Purchase> getAllPurchasesOfUsername(String username);
    List<User> getUserSpendingMoreThan(float mount);
    List<Supplier> getTopNSuppliersInPurchases(int n);
    long getCountOfPurchasesBetweenDates(Date start, Date end);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutsNotSell();
    List<Route> getTop3RoutesWithMaxRating();
    Service getMostDemandedService();
    List<TourGuideUser> getTourGuidesWithRating1();
    Service addServiceToSupplier(Service service, Supplier supplier) throws ToursException;
}
