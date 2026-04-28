package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
@Entity
@DiscriminatorValue("TOUR_GUIDE")
public class TourGuideUser extends User {
    @Column(nullable=true)
    private String education;

    @ManyToMany(mappedBy = "tourGuideList", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Route> routes = new ArrayList<Route>();


    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }
    public void addRoute(Route route) {
        this.routes.add(route);
        route.addTourGuide(this);
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
    public TourGuideUser(String username, String password, String fullName, String email, java.util.Date birthdate,
            String phoneNumber, String education) {
        super(username, password, fullName, email, birthdate, phoneNumber);
        this.education = education;
    }
    public TourGuideUser() {
        super();
    }

    
}
