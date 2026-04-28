package unlp.info.bd2.model;

import jakarta.persistence.*;

@Entity
public class ItemService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    public ItemService(Service service2, int quantity2, Purchase purchase2) {
        this.service = service2;
        this.quantity = quantity2;
        this.purchase = purchase2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
