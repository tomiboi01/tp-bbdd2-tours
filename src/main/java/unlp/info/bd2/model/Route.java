package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private float price;

    private float totalKm;

    private int maxNumberUsers;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "route_id")
    private List<Stop> stops = new java.util.ArrayList<Stop>();

    @ManyToMany
    @JoinTable(name="DriverUser_Route", 
    joinColumns=@JoinColumn(name="route_id", referencedColumnName="id"),
    inverseJoinColumns=@JoinColumn(name="driver_id", referencedColumnName="id"))
    private List<DriverUser> driverList = new java.util.ArrayList<DriverUser>();

    @ManyToMany
    @JoinTable(name="TourGuideUser_Route", 
    joinColumns=@JoinColumn(name="route_id", referencedColumnName="id"),
    inverseJoinColumns=@JoinColumn(name="tour_guide_id", referencedColumnName="id"))
    private List<TourGuideUser> tourGuideList = new java.util.ArrayList<TourGuideUser>();

    public Route(String name2, float price2, float totalKm2, int maxNumberOfUsers, List<Stop> stops2) {
        this.name = name2;
        this.price = price2;
        this.totalKm = totalKm2;
        this.maxNumberUsers = maxNumberOfUsers;
        this.stops = stops2;
    }
    public void addStop(Stop stop) {
        this.stops.add(stop);
    }
    public void addDriver(DriverUser driver) {
        this.driverList.add(driver);
    }
    public void addTourGuide(TourGuideUser tourGuide) {
        this.tourGuideList.add(tourGuide);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public int getMaxNumberUsers() {
        return maxNumberUsers;
    }

    public void setMaxNumberUsers(int maxNumberUsers) {
        this.maxNumberUsers = maxNumberUsers;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<DriverUser> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<DriverUser> driverList) {
        this.driverList = driverList;
    }

    public List<TourGuideUser> getTourGuideList() {
        return tourGuideList;
    }

    public void setTourGuideList(List<TourGuideUser> tourGuideList) {
        this.tourGuideList = tourGuideList;
    }

public Route() {  }
}
