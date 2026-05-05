package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.*;
@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;
    @Column(unique=true, nullable=false)
    private String authorizationNumber;

    @OneToMany(mappedBy = "supplier")
    private List<Service> services = new java.util.ArrayList<Service>();

    public Supplier(String businessName2, String authorizationNumber2) {
        this.businessName = businessName2;
        this.authorizationNumber = authorizationNumber2;
    }
    public Supplier() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public List<Service> getServices() {
        return services;
    }
    public void addService(Service service) {
        this.services.add(service);
        service.setSupplier(this);
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
