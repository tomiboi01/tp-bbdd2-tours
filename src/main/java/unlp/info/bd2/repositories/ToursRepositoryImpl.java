package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.jpa.ItemServiceRepository;
import unlp.info.bd2.repositories.jpa.PurchaseRepository;
import unlp.info.bd2.repositories.jpa.ReviewRepository;
import unlp.info.bd2.repositories.jpa.RouteRepository;
import unlp.info.bd2.repositories.jpa.ServiceRepository;
import unlp.info.bd2.repositories.jpa.StopRepository;
import unlp.info.bd2.repositories.jpa.SupplierRepository;
import unlp.info.bd2.repositories.jpa.UserRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemServiceRepository itemServiceRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public User createUser(User user) throws ToursException {
        return userRepository.save(user);
    }

    @Override
    public DriverUser createDriverUser(DriverUser driverUser) throws ToursException {
        return userRepository.save(driverUser);
    }

    @Override
    public TourGuideUser createTourGuideUser(TourGuideUser tourGuideUser) throws ToursException {
        return userRepository.save(tourGuideUser);
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        userRepository.delete(user);
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        return stopRepository.save(new Stop(name, description));
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        return stopRepository.findByNameStartingWith(name);
    }

    @Override
    public Route createRoute(Route route) throws ToursException {
        return routeRepository.save(route);
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.findByPriceLessThan(price);
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ToursException("User not found with username: " + username));
        if (!(user instanceof DriverUser driverUser)) {
            throw new ToursException("User with username " + username + " is not a DriverUser");
        }
        Route route = routeRepository.findById(idRoute)
                .orElseThrow(() -> new ToursException("Route not found with id: " + idRoute));
        driverUser.addRoute(route);
        routeRepository.save(route);
        userRepository.save(driverUser);
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ToursException("User not found with username: " + username));
        if (!(user instanceof TourGuideUser tourGuideUser)) {
            throw new ToursException("User with username " + username + " is not a TourGuideUser");
        }
        Route route = routeRepository.findById(idRoute)
                .orElseThrow(() -> new ToursException("Route not found with id: " + idRoute));
        tourGuideUser.addRoute(route);
        routeRepository.save(route);
        userRepository.save(tourGuideUser);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) throws ToursException {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) throws ToursException {
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Supplier supplier) throws ToursException {
        supplierRepository.delete(supplier);
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ToursException("Service not found with id: " + id));
        service.setPrice(newPrice);
        return serviceRepository.save(service);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    public Service addServiceToSupplier(Service service, Supplier supplier) throws ToursException {
        service.setSupplier(supplier);
        supplier.addService(service);
        return serviceRepository.save(service);
    }

    @Override
    public Purchase createPurchase(Purchase purchase) throws ToursException {
        return purchaseRepository.save(purchase);
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService itemService = new ItemService(service, quantity, purchase);
        purchase.getItemServiceList().add(itemService);
        return itemServiceRepository.save(itemService);
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        return purchaseRepository.findByCode(code);
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchaseRepository.delete(purchase);
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = new Review(rating, comment, purchase);
        purchase.setReview(review);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        routeRepository.delete(route);
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return purchaseRepository.findByUserUsername(username);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return purchaseRepository.findUsersSpendingMoreThan(mount);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return purchaseRepository.findTopSuppliersInPurchases(PageRequest.of(0, n));
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.countPurchasesBetweenDates(start, end);
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return routeRepository.findRoutesWithStop(stop.getId());
    }

    @Override
    public Long getMaxStopOfRoutes() {
        return routeRepository.findMaxStopOfRoutes();
    }

    @Override
    public List<Route> getRoutsNotSell() {
        return routeRepository.findRoutsNotSell();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return routeRepository.findTop3RoutesWithMaxRating(PageRequest.of(0, 3));
    }

    @Override
    public Service getMostDemandedService() {
        return serviceRepository.findMostDemandedService(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.findTourGuidesWithRating1();
    }
}