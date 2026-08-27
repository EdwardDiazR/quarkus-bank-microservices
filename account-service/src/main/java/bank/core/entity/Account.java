package bank.core.entity;

import bank.core.entity.dto.request.CreateAccountRequest;
import bank.core.enums.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account", indexes = {

})
public class Account extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @Column(name = "public_number")
    public String publicNumber;
    @Column(name = "product_code")
    public int productCode;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    public Currency currency;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public AccountType type;

    @Column(name = "status")
    public AccountStatus status;

    @Column(name = "has_first_deposit")
    public boolean hasFirstDeposit;

    @Column(name = "ownership_type")
    @Enumerated(EnumType.STRING)
    public AccountOwnerShipType ownerShipType;

    @Column(name = "signature_type")
    @Enumerated(EnumType.STRING)
    public SignatureType signatureType;

    /*Balances*/
    @Column(name = "total_balance", precision = 20, scale = 2)
    public BigDecimal totalBalance;

    @Column(name = "blocked_balance", precision = 20, scale = 2)
    public BigDecimal blockedBalance;

    @Column(name = "in_transit_balance", precision = 20, scale = 2)
    public BigDecimal inTransitBalance;

    @Column(name = "available_balance", precision = 20, scale = 2)
    public BigDecimal availableBalance;

    @Column(name = "generated_interest", precision = 20, scale = 2)
    public BigDecimal generatedInterest;

    /*Dates*/
    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "last_deposit_at")
    public LocalDateTime lastDepositDate;
    @Column(name = "last_withdraw_at")
    public LocalDateTime lastWithdrawDate;
    @Column(name = "last_activity_at")
    public LocalDateTime lastActivityDate;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "account",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<AccountHolder> holders = new ArrayList<>();



    public static Account openAccount(String accountNumber, CreateAccountRequest accountRequest, List<AccountHolder> holders) {
        Account account = new Account();

        account.productCode = accountRequest.productCode();
        account.publicNumber = accountNumber;
        account.currency = accountRequest.currencyCode();
        account.type = accountRequest.type();
        account.status = AccountStatus.ACTIVE;
        account.hasFirstDeposit = false;
        account.totalBalance = BigDecimal.ZERO;
        account.blockedBalance = BigDecimal.ZERO;
        account.inTransitBalance = BigDecimal.ZERO;
        account.availableBalance = BigDecimal.ZERO;
        account.lastDepositDate = null;
        account.lastActivityDate = null;
        account.lastWithdrawDate = null;
        account.generatedInterest = BigDecimal.ZERO;
        account.createdAt = LocalDateTime.now();

        account.ownerShipType = accountRequest.ownershipType();
        account.signatureType = accountRequest.signatureType();

        holders.forEach((holder) -> {
            holder.account = account;
            account.addHolder(holder);
        });

        return account;

    }

    public void addHolder(AccountHolder holder) {
        holders.add(holder);
        holder.account = this;
    }

    public void debitForTransfer(Money money) {
    }

    public Money withdraw(Money money) {
        validateOperation();

        BigDecimal cleanAmount = normalizeAmount(money.amount);

        if (!money.currency.equals(this.currency)) {
            throw new IllegalStateException("Currencies don't match");
        }

        if (this.availableBalance.compareTo(cleanAmount) < 0) {
            throw new IllegalStateException("Insufficient fund to withdraw");
        }

        this.availableBalance = this.availableBalance.subtract(cleanAmount);
        this.lastActivityDate = LocalDateTime.now();
        this.lastWithdrawDate = LocalDateTime.now();

        return new Money(cleanAmount, money.currency);
    }


    public void deposit(Money money) {
        validateOperation();

        if (!money.currency.equals(currency)) {
            throw new IllegalStateException("Currencies don't match");
        }

        if (money.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Amount must be bigger than zero");
        }

        if (!this.hasFirstDeposit && !isAmountEqualOpeningDeposit(money, this.currency)) {
            throw new IllegalStateException("The initial deposit amount does not match the required opening balance.");
        }

        BigDecimal cleanAmount = normalizeAmount(money.amount);

        this.hasFirstDeposit = true;
        totalBalance = this.totalBalance.add(cleanAmount);
        this.lastDepositDate = LocalDateTime.now();
        this.lastActivityDate = LocalDateTime.now();
        recalculateAvailableBalance();

    }

    private void recalculateAvailableBalance(){

        this.availableBalance = this.totalBalance
                .subtract(this.blockedBalance)
                .subtract(this.inTransitBalance)
                .setScale(2,RoundingMode.HALF_EVEN);
    }

    private boolean isAmountEqualOpeningDeposit(Money money, Currency currency) {
        //todo: check amount by product type
        BigDecimal FIRST_DEPOSIT_AMOUNT = new BigDecimal("500.00").setScale(2, RoundingMode.HALF_EVEN);

        return money.amount.compareTo(FIRST_DEPOSIT_AMOUNT) >= 0;
    }

    public void block() {
        if (status == AccountStatus.BLOCKED) {
            throw new RuntimeException("Account is already blocked");
        }
        status = AccountStatus.BLOCKED;
    }

    public void activate() {
        if (status == AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is already active");
        }
        status = AccountStatus.ACTIVE;
    }

    public void restrict() {
        if (status == AccountStatus.RESTRICTED) {
            throw new RuntimeException("Account is already restricted");
        }
        status = AccountStatus.RESTRICTED;
    }

    public void close() {
        if (status == AccountStatus.CANCELLED) {
            throw new IllegalStateException("Account is already cancelled");
        }

        boolean haveAvailableBalance = this.availableBalance.compareTo(BigDecimal.ZERO) != 0;
        boolean haveInTransitBalance = this.inTransitBalance.compareTo(BigDecimal.ZERO) != 0;
        boolean haveBlockedAmount = this.blockedBalance.compareTo(BigDecimal.ZERO) != 0;

        if (haveAvailableBalance || haveInTransitBalance || haveBlockedAmount) {
            throw new IllegalStateException("Account's balance must be in zero to cancel");
        }

        status = AccountStatus.CANCELLED;
        lastActivityDate = LocalDateTime.now();
    }

    private void validateOperation() {
        if (this.status == AccountStatus.CANCELLED) {
            throw new IllegalStateException("Operación no permitida: la cuenta está cancelada");
        }
        if (this.status == AccountStatus.BLOCKED) {
            throw new IllegalStateException("Operación no permitida: cuenta bloqueada por prevención de lavado (Ley 155-17)");
        }
        if (this.status == AccountStatus.INACTIVE) {
            throw new IllegalStateException("Operación no permitida: la cuenta está inactiva");
        }
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de la operación debe ser un valor mayor a cero.");
        }

        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
