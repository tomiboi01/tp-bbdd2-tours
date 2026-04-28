package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public class ToursRepositoryImpl implements ToursRepository {
    @Autowired
    protected SessionFactory sessionFactory;
    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        return user;
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        Session session = sessionFactory.getCurrentSession();
        session.persist(driverUser);
        return driverUser;
    }


    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        Session session = sessionFactory.getCurrentSession();
        session.persist(tourGuideUser);
        return tourGuideUser;
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("FROM User WHERE username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        return session.merge(user);
    }

    @Override

    public void deleteUser(User user) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        session.remove(user);
    }

    @Override

    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop(name, description);
        Session session = sessionFactory.getCurrentSession();
        session.persist(stop);
        return stop;
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Stop WHERE name LIKE :name", Stop.class)
                .setParameter("name", name + "%")
                .getResultList();
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        Session session = sessionFactory.getCurrentSession();
        session.persist(route);
        return route;
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Route route = session.get(Route.class, id);
        return Optional.ofNullable(route);
    }
    

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Route WHERE price < :price", Route.class)
                .setParameter("price", price)
                .getResultList();
    }   

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignDriverByUsername'");
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignTourGuideByUsername'");
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        Session session = sessionFactory.getCurrentSession();
        session.persist(supplier);
        return supplier;
    }

    @Override
    public Service addServiceToSupplier(Service service, Supplier supplier)
            throws ToursException {
        supplier.addService(service);
        service.setSupplier(supplier);
        Session session = sessionFactory.getCurrentSession();
        session.persist(service);
        return service;
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        Service service = session.get(Service.class, id);
        if (service == null) {
            throw new ToursException("Service not found with id: " + id);
        }
        service.setPrice(newPrice);
        session.merge(service);
        return service;
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.get(Supplier.class, id);
        return Optional.ofNullable(supplier);
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.createQuery("FROM Supplier WHERE authorizationNumber = :authorizationNumber", Supplier.class)
                .setParameter("authorizationNumber", authorizationNumber)
                .getSingleResultOrNull();
        return Optional.ofNullable(supplier);
    }
    

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        Service service = session.createQuery("FROM Service WHERE name = :name AND supplier.id = :supplierId", Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", id)
                .getSingleResultOrNull();
        return Optional.ofNullable(service);
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, route, user);
        Session session = sessionFactory.getCurrentSession();
        session.persist(purchase);
        return purchase;
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, date, route, user);
        Session session = sessionFactory.getCurrentSession();
        user.getPurchaseList().add(purchase);
        session.persist(purchase);
        session.refresh(user);

        return purchase;
    }
    

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService itemService = new ItemService(service, quantity, purchase);
        Session session = sessionFactory.getCurrentSession();
        session.persist(itemService);
        return itemService;
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        Session session = sessionFactory.getCurrentSession();
        Purchase purchase = session.createQuery("FROM Purchase WHERE code = :code", Purchase.class)
                .setParameter("code", code)
                .getSingleResultOrNull();
        return Optional.ofNullable(purchase);
    }
    

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        session.remove(purchase);
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = new Review(rating, comment, purchase);
        Session session = sessionFactory.getCurrentSession();
        session.persist(review);
        return review;
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        session.remove(route);
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Purchase WHERE user.username = :username", Purchase.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p.user FROM Purchase p GROUP BY p.user HAVING SUM(p.totalPrice) > :mount", User.class)
                .setParameter("mount", mount)
                .getResultList();
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p.itemServiceList.service.supplier FROM Purchase p JOIN p.itemServiceList GROUP BY p.itemServiceList.service.supplier ORDER BY COUNT(p) DESC", Supplier.class)
                .setMaxResults(n)
                .getResultList();
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT COUNT(*) FROM Purchase WHERE date BETWEEN :start AND :end", Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT r FROM Route r JOIN r.stops s WHERE s.id = :stopId", Route.class)
                .setParameter("stopId", stop.getId())
                .getResultList();
    }

    @Override
    public Long getMaxStopOfRoutes() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT MAX(COUNT(s)) FROM Route r JOIN r.stops s GROUP BY r.id", Long.class)
                .getSingleResult();
    }

    @Override
    public List<Route> getRoutsNotSell() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT r FROM Route r LEFT JOIN Purchase p ON r.id = p.route.id WHERE p.id IS NULL", Route.class)
                .getResultList();
        

    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT r FROM Route r JOIN Purchase p ON r.id = p.route.id JOIN Review rev ON p.id = rev.purchase.id GROUP BY r.id ORDER BY AVG(rev.rating) DESC", Route.class)
                .setMaxResults(3)
                .getResultList();

    }

    @Override
    public Service getMostDemandedService() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT s FROM Service s JOIN ItemService is ON s.id = is.service.id GROUP BY s.id ORDER BY SUM(is.quantity) DESC", Service.class)
                .setMaxResults(1)
                .getSingleResultOrNull();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT u FROM TourGuideUser u JOIN Review r ON u.id = r.purchase.user.id WHERE r.rating = 1", TourGuideUser.class)
                .getResultList();
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        return session.merge(supplier);
    }

    @Override
    public void deleteSupplier(Supplier supplier) throws ToursException {
        Session session = sessionFactory.getCurrentSession();
        session.remove(supplier);
    }


    

    
    
}
