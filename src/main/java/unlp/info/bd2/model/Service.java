package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private float price;

    private String description;
    
    @OneToMany(mappedBy = "service")
    private List<ItemService> itemServiceList = new ArrayList<ItemService>();

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;


    public Service(String name2, float price2, String description2, Supplier supplier) {
        this.name = name2;
        this.price = price2;
        this.description = description2;
        this.supplier = supplier;
        this.itemServiceList = new ArrayList<ItemService>();
    }
    public Service() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
