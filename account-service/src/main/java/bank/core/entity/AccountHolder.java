package bank.core.entity;


import bank.core.enums.ParticipantType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "account_holder")
public class AccountHolder extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    public UUID id;
    @Column(name = "customer_id")
    public Long customerId;
    @Column(name = "full_name")
    public String fullName;

    @Column(name = "national_id")
    public String nationalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    public Account account;

    @Column(name = "participant_type")
    @Enumerated(EnumType.STRING)
    public ParticipantType participantType;
//    public int signatureOrder;

    @Column(name = "is_active")
    public boolean isActive;
}
