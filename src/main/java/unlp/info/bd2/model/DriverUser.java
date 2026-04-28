package unlp.info.bd2.model;


import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DRIVER")
public class DriverUser extends User {
    @Column(nullable=true)
    private String expedient;

    @ManyToMany(mappedBy = "driverList", cascade = { CascadeType.PERSIST, CascadeType.MERGE }) 
    private List<Route> routes = new java.util.ArrayList<Route>();

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) {
        this.routes = routs;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
        route.addDriver(this);
    }

    public DriverUser() {
        super();
    }
    public DriverUser(String username, String password, String fullName, String email, java.util.Date birthdate,
            String phoneNumber, String expedient) {
        super(username, password, fullName, email, birthdate, phoneNumber);
        this.expedient = expedient;
    }
}
