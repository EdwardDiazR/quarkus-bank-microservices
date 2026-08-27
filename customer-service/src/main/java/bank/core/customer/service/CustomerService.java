package bank.core.customer.service;

import bank.core.customer.dto.request.CreateCustomerRequest;
import bank.core.customer.dto.request.CustomerAddressRequest;
import bank.core.customer.dto.request.CustomerEmailRequest;
import bank.core.customer.dto.request.CustomerPhoneRequest;
import bank.core.customer.dto.response.CustomerAccountValidationResponse;
import bank.core.customer.entity.Customer;
import bank.core.customer.entity.CustomerAddress;
import bank.core.customer.entity.CustomerEmail;
import bank.core.customer.entity.CustomerPhone;
import bank.core.customer.enums.CustomerStatus;
import bank.core.customer.exceptions.BusinessException;
import bank.core.customer.exceptions.CustomerAlreadyExistsException;
import bank.core.customer.exceptions.CustomerNotFoundException;
import bank.core.customer.interfaces.ICustomerService;
import bank.core.customer.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@ApplicationScoped
public class CustomerService implements ICustomerService {

    @Inject
    CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.listAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
    }

    public Customer findByDocumentNumber(String documentNumber) {
        return customerRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new CustomerNotFoundException(documentNumber));
    }

    @Transactional
    public Customer create(CreateCustomerRequest dto) {

        Customer customer = new Customer();
        customer.documentNumber = dto.documentNumber();
        customer.documentType = dto.documentType();
        customer.nationality = dto.nationality();
        customer.personType = dto.personType();
        customer.firstName = dto.firstName();
        customer.middleName = dto.middleName();
        customer.lastName = dto.lastName();
        customer.secondLastName = dto.secondLastName();
        customer.isPep = dto.isPep();
        customer.gender = dto.gender();
        customer.birthDate = dto.birthDate();
        customer.age = calculateAge(dto.birthDate());
        customer.kycStatus = "PENDING";
        customer.amlRiskLevel = dto.isPep() ? 3 : 1;
        customer.isKycCompleted = false;
        customer.status = CustomerStatus.ACTIVE;
        customer.monthlyIncome = dto.monthlyIncome();

        validateCustomer(customer);

        customerRepository.persist(customer);

        saveEmails(dto.contacts().emails(), customer);
        savePhones(dto.contacts().phones(), customer);
        saveAddresses(dto.contacts().addresses(), customer);

        return customer;
    }

    private void saveEmails(List<CustomerEmailRequest> emailRequests, Customer customer) {
        emailRequests.forEach(email -> {
            CustomerEmail customerEmail = new CustomerEmail();
            customerEmail.email = email.email();
            customerEmail.primary = email.primary();
            customerEmail.type = email.type();
            customerEmail.customer = customer;

            customerEmail.persist();
        });
    }

    private void savePhones(List<CustomerPhoneRequest> phoneRequests, Customer customer) {
        phoneRequests.forEach(phone -> {
            CustomerPhone customerPhone = new CustomerPhone();
            customerPhone.countryCode = phone.countryCode();
            customerPhone.customer = customer;
            customerPhone.type = phone.type();
            customerPhone.primary = phone.primary();
            customerPhone.phoneNumber = phone.phoneNumber();

            customerPhone.persist();
        });
    }

    private void saveAddresses(List<CustomerAddressRequest> addressRequests, Customer customer) {
        addressRequests.forEach(address -> {
            CustomerAddress customerAddress = new CustomerAddress();
            customerAddress.city = address.city();
            customerAddress.customer = customer;
            customerAddress.primary = address.primary();
            customerAddress.type = address.type();
            customerAddress.postalCode = address.postalCode();
            customerAddress.province = address.province();
            customerAddress.reference = address.reference();
            customerAddress.sector = address.sector();
            customerAddress.street = address.street();

            customerAddress.persist();
        });
    }

    @Transactional
    public void update(Long id, Customer customer) {

        Customer existingCustomer = customerRepository.findById(id);

        if (existingCustomer == null) {
            throw new CustomerNotFoundException(id.toString());
        }

        existingCustomer.documentNumber = customer.documentNumber;
        existingCustomer.documentType = customer.documentType;
        existingCustomer.firstName = customer.firstName;
        existingCustomer.middleName = customer.middleName;
        existingCustomer.lastName = customer.lastName;
        existingCustomer.secondLastName = customer.secondLastName;
        existingCustomer.birthDate = customer.birthDate;
        existingCustomer.gender = customer.gender;
        existingCustomer.status = customer.status;

//      return existingCustomer;
    }

    @Transactional
    public boolean delete(Long id) {
        return customerRepository.deleteById(id);
    }

    private void validateCustomer(Customer customer) {
        //todo: then create customer DTO and validate
        boolean existByDocumentNumber = customerRepository.existsByDocumentNumber(customer.documentNumber);


        if (existByDocumentNumber) {
            throw new CustomerAlreadyExistsException(customer.documentNumber);
        }

        if (customer.documentType.equalsIgnoreCase("CEDULA") && customer.documentNumber.length() != 11) {
            throw new BusinessException("INVALID DOCUMENT ID", "Cedula must have 11 digits", Response.Status.BAD_REQUEST);
        }

        if (calculateAge(customer.birthDate) < 18) {
            throw new BusinessException("CUSTOMER_UNDER_AGE", "Customer's age is under 18 years old", Response.Status.BAD_REQUEST);
        }

    }

    private int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();

        return Period.between(dob, currentDate).getYears();

    }

    public CustomerStatus getStatusByCustomerId(Long customerId) {
        return customerRepository.getStatusById(customerId);
    }

    @Transactional
    public void updateCustomerStatus(Long customerId, CustomerStatus newStatus) {
        Customer customer = customerRepository.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(customerId.toString());
        }

        customer.updateStatus(newStatus);

        customerRepository.persist(customer);
        //todo: auditoria o trazabilidad de quien ejecuto la accion
    }

    @Override
    public String getRiskLevelByCustomerId(Long customerId) {
        int riskLevel = customerRepository.getRiskLevelById(customerId);
        return switch (riskLevel) {
            case 1 -> "LOW";
            case 2 -> "MEDIUM";
            case 3 -> "HIGH";
            default -> "UNKNOWN";
        };
    }

    @Transactional
    public void updateCustomerRiskLevel(Long customerId, int newRiskLevel) {
        Customer customer = customerRepository.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(customerId.toString());
        }

        customer.updateRiskLevel(newRiskLevel);

        customerRepository.persist(customer);
        //todo: auditoria o trazabilidad de quien ejecuto la accion
    }

    public CustomerAccountValidationResponse getCustomerForAccount(Long customerId) {
        Customer customer = customerRepository.findById(customerId);

        String firstName = customer.firstName;
        String middleName = !customer.middleName.isEmpty() ? customer.middleName : "";
        String lastName = customer.lastName;
        String secondLastName = !customer.secondLastName.isEmpty() ? customer.secondLastName : "";

        String fullName = String.join(" ", customer.firstName, customer.lastName);
        return new CustomerAccountValidationResponse(customer.id,customer.documentNumber, fullName, customer.status);

    }
}