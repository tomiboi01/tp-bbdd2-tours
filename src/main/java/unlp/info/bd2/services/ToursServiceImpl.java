package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

@Service
public class ToursServiceImpl implements ToursService {
    @Autowired
    private ToursRepository toursRepository;

    public ToursServiceImpl(ToursRepository repository) {
        this.toursRepository = repository;
    }

    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
            if (alreadyRegisteredName(username)) {
                throw new ToursException("Username already exists: " + username);
            }
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        return toursRepository.createUser(user);
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        if (alreadyRegisteredName(username)) {
                throw new ToursException("Username already exists: " + username);
            }
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        return toursRepository.createDriverUser(driverUser);
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
            if (alreadyRegisteredName(username)) {
                throw new ToursException("Username already exists: " + username);
            }
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        return toursRepository.createTourGuideUser(tourGuideUser);
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return toursRepository.getUserById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return toursRepository.getUserByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        User userWithUsername = toursRepository.getUserByUsername(user.getUsername()).orElse(null);
        if (userWithUsername != null && !userWithUsername.getId().equals(user.getId())) {
            throw new ToursException("Username already exists: " + user.getUsername());
        }
        return toursRepository.updateUser(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        toursRepository.deleteUser(user);
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        return toursRepository.createStop(name, description);
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        return toursRepository.getStopByNameStart(name);
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        return toursRepository.createRoute(route);
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        
        return toursRepository.getRouteById(id);
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return toursRepository.getRoutesBelowPrice(price);
        
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        User user = toursRepository.getUserByUsername(username)
                .orElseThrow(() -> new ToursException("User not found with username: " + username));
        if (!(user instanceof DriverUser)) {
            throw new ToursException("User with username " + username + " is not a DriverUser");
        }
        Route route = toursRepository.getRouteById(idRoute)
                .orElseThrow(() -> new ToursException("Route not found with id: " + idRoute));
        DriverUser driverUser = (DriverUser) user;
        driverUser.addRoute(route);
        toursRepository.updateUser(driverUser);
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        User user = toursRepository.getUserByUsername(username)
                .orElseThrow(() -> new ToursException("User not found with username: " + username));
        if (!(user instanceof TourGuideUser)) {
            throw new ToursException("User with username " + username + " is not a TourGuideUser");
        }
        Route route = toursRepository.getRouteById(idRoute)
                .orElseThrow(() -> new ToursException("Route not found with id: " + idRoute));
        TourGuideUser tourGuideUser = (TourGuideUser) user;
        tourGuideUser.addRoute(route);
        toursRepository.updateUser(tourGuideUser);
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if (toursRepository.getSupplierByAuthorizationNumber(authorizationNumber).isPresent()) {
            throw new ToursException("Supplier with authorization number " + authorizationNumber + " already exists");
        }
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        return toursRepository.createSupplier(supplier);
    }

    @Override
    public unlp.info.bd2.model.Service addServiceToSupplier(String name, float price, String description,
            Supplier supplier) throws ToursException {
        unlp.info.bd2.model.Service service = new unlp.info.bd2.model.Service(name, price, description, supplier);
        toursRepository.addServiceToSupplier(service,supplier);

        return service;
}

    @Override
    public unlp.info.bd2.model.Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        return toursRepository.updateServicePriceById(id, newPrice);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return toursRepository.getSupplierById(id);
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return toursRepository.getSupplierByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<unlp.info.bd2.model.Service> getServiceByNameAndSupplierId(String name, Long id)
        throws ToursException {
        return toursRepository.getServiceByNameAndSupplierId(name, id);
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, route, user);
        return toursRepository.createPurchase(purchase);
    }
    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, date, route, user);
        return toursRepository.createPurchase(purchase);
    }

    @Override
    public ItemService addItemToPurchase(unlp.info.bd2.model.Service service, int quantity, Purchase purchase)
            throws ToursException {
        return toursRepository.addItemToPurchase(service, quantity, purchase);
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        return toursRepository.getPurchaseByCode(code);
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        toursRepository.deletePurchase(purchase);
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        return toursRepository.addReviewToPurchase(rating, comment, purchase);
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        toursRepository.deleteRoute(route);
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return toursRepository.getAllPurchasesOfUsername(username);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return toursRepository.getUserSpendingMoreThan(mount);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return toursRepository.getTopNSuppliersInPurchases(n);
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return toursRepository.getCountOfPurchasesBetweenDates(start, end);
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return toursRepository.getRoutesWithStop(stop);
    }

    @Override
    public Long getMaxStopOfRoutes() {
        return toursRepository.getMaxStopOfRoutes();
    }

    @Override
    public List<Route> getRoutsNotSell() {
        return toursRepository.getRoutsNotSell();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return toursRepository.getTop3RoutesWithMaxRating();
    }

    @Override
    public unlp.info.bd2.model.Service getMostDemandedService() {
        return toursRepository.getMostDemandedService();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return toursRepository.getTourGuidesWithRating1();
    }

    private boolean alreadyRegisteredName(String username) {
        try {
            return toursRepository.getUserByUsername(username).isPresent();
        } catch (ToursException e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
            return false; // O lanzar una excepción personalizada
        }
    }
    
    
}
