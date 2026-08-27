package bank.core.customer.interfaces;

import bank.core.customer.dto.request.CreateCustomerRequest;
import bank.core.customer.dto.response.CustomerAccountValidationResponse;
import bank.core.customer.entity.Customer;
import bank.core.customer.enums.CustomerStatus;

import java.util.List;

public interface ICustomerService {

    List<Customer> findAll();
    Customer findById(Long id);
    Customer create(CreateCustomerRequest customer);
    Customer findByDocumentNumber(String documentNumber);
    void update(Long id, Customer customer);
    CustomerStatus getStatusByCustomerId(Long id);
    void updateCustomerStatus(Long customerId,CustomerStatus newStatus);
    String getRiskLevelByCustomerId(Long customerId);
    void updateCustomerRiskLevel(Long customerId, int newRiskLevel);
    CustomerAccountValidationResponse getCustomerForAccount(Long customerId);
}
