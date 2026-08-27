package bank.core.customer.entity;

import bank.core.customer.enums.AddressType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_address")
public class CustomerAddress extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    public AddressType type;

    @Column(name = "street", nullable = false)
    public String street;

    @Column(name = "sector", nullable = false)
    public String sector;

    @Column(name = "city", nullable = false)
    public String city;

    @Column(name = "province", nullable = false)
    public String province;

    @Column(name = "postal_code")
    public String postalCode;

    @Column(name = "reference")
    public String reference;

    @Column(name = "is_primary", nullable = false)
    public boolean primary;

}
