package bank.core.customer.entity;

import bank.core.customer.enums.PhoneType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_phone")
public class CustomerPhone extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type", nullable = false)
    public PhoneType type;

    @Column(name = "phone_number", nullable = false)
    public String phoneNumber;

    @Column(name = "country_code")
    public String countryCode;

    @Column(name = "is_primary", nullable = false)
    public boolean primary;


}