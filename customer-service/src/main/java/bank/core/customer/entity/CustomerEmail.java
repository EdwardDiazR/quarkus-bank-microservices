package bank.core.customer.entity;

import bank.core.customer.enums.EmailType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_email")
public class CustomerEmail extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false)
    public EmailType type;

    @Column(nullable = false)
    public String email;

    @Column(name = "is_primary", nullable = false)
    public boolean primary;

}