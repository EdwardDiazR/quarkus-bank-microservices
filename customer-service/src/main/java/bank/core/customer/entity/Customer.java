package bank.core.customer.entity;

import bank.core.customer.enums.CustomerStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
public class Customer extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @NotBlank(message = "Document number is required")
    @Size(max = 20, message = "Document number cannot exceed 20 characters")
    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    public String documentNumber;

    @NotBlank(message = "Document type is required")
    @Column(name = "document_type", nullable = false, length = 10)
    public String documentType;

    @NotBlank(message = "Nationality is required")
    @Size(min = 2, max = 3)
    @Column(name = "nationality", nullable = false, length = 3)
    public String nationality; // ISO Code e.g., "DOM"

    @NotBlank(message = "Person type is required")
    @Column(name = "person_type", nullable = false, length = 10)
    public String personType;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    public String firstName;

    @Size(max = 50)
    @Column(name = "middle_name", length = 50)
    public String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    public String lastName;

    @Size(max = 50)
    @Column(name = "second_last_name", length = 50)
    public String secondLastName;

    @Column(name = "is_pep", nullable = false)
    public boolean isPep = false;

    @NotBlank(message = "KYC status is required")
    @Column(name = "kyc_status", nullable = false, length = 20)
    public String kycStatus = "PENDING"; // PENDING, APPROVED, REJECTED, EXPIRED

    @Column(name = "kyc_last_updated_at")
    public LocalDateTime kycLastUpdatedAt;

    @Column(name = "gender", nullable = false, length = 1)
    public String gender; // 'M', 'F'

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date", nullable = false)
    public LocalDate birthDate;

    @Min(0)
    @Max(120)
    @Column(name = "age", nullable = false)
    public int age;

    @Min(1)
    @Max(100)
    @Column(name = "aml_risk_level", nullable = false)
    public int amlRiskLevel = 1;

    @Column(name = "is_kyc_completed", nullable = false)
    public boolean isKycCompleted = false;

    @Column(name = "monthly_income", precision = 20, scale = 2)
    public BigDecimal monthlyIncome;

    @NotNull(message = "Customer status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    public CustomerStatus status = CustomerStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "last_update_at")
    public LocalDateTime lastUpdateAt;

   /* @Column(name = "bank_executive_id")
    public Long bankExecutiveId;*/


    public void updateStatus(CustomerStatus newStatus) {
        if (this.status.equals(newStatus)) {
            throw new IllegalArgumentException("New status equals previous status");
        }
        this.status = newStatus;
    }

    public void updateRiskLevel(int newRiskLevel) {
        if (this.amlRiskLevel == newRiskLevel) {
            throw new IllegalArgumentException("New value equals previous status");
        }
        this.amlRiskLevel = newRiskLevel;
    }


    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdateAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.lastUpdateAt = LocalDateTime.now();
    }
}