package bank.core.customer.repository;

import bank.core.customer.entity.Customer;
import bank.core.customer.enums.CustomerStatus;
import bank.core.customer.exceptions.CustomerNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public boolean existsByDocumentNumber(String documentNumber) {
        return count("documentNumber", documentNumber) > 0;
    }

    public Optional<Customer> findByDocumentNumber(String documentNumber) {
        return find("documentNumber", documentNumber)
                .firstResultOptional();
    }

    public CustomerStatus getStatusById(Long customerId) {
        return find("id", customerId)
                .firstResultOptional()
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"))
                .status;
    }

    public int getRiskLevelById(Long customerId) {
        return find("id", customerId)
                .firstResultOptional()
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"))
                .amlRiskLevel;
    }


}
