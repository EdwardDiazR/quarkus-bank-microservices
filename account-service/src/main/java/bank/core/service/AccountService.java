package bank.core.service;

import bank.core.clients.CustomerClient;
import bank.core.entity.dto.response.CustomerResponseForAccount;
import bank.core.entity.Account;
import bank.core.entity.AccountHolder;
import bank.core.entity.dto.request.AccountHolderRequest;
import bank.core.entity.dto.response.AccountResponse;
import bank.core.entity.dto.request.CreateAccountRequest;
import bank.core.enums.AccountOwnerShipType;
import bank.core.enums.ParticipantType;
import bank.core.enums.SignatureType;
import bank.core.exceptions.*;
import bank.core.mappers.AccountMapper;
import bank.core.mappers.HolderMapper;
import bank.core.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ApplicationScoped
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Inject
    @RestClient
    CustomerClient customerClient;

    @Inject
    AccountRepository accountRepository;

    @Inject
    HolderMapper holderMapper;
    @Inject
    AccountMapper accountMapper;


    public AccountResponse getById(Long id) {
        String notFoundErrorMessage = String.format("ACCOUNT ID: %s NOT FOUND", id.toString());

        Account account = accountRepository.findByIdOptional(id)
                .orElseThrow(() -> new AccountNotFoundException(notFoundErrorMessage));

        return accountMapper.toResponse(account);
    }

    public AccountResponse getByNumber(String accountNumber) {

        String notFoundErrorMessage = String.format("ACCOUNT No.: %s NOT FOUND", accountNumber);
        Account account = accountRepository.findByPublicNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(notFoundErrorMessage));

        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest accountRequest) {

        validateOwnerShip(accountRequest);

        List<AccountHolder> holders = validateAndBuildHolders(accountRequest.holders());

        Account account = Account.openAccount("1234", accountRequest, holders);

        accountRepository.persist(account);

        log.info("ACCOUNT-CREATED: {}", account.publicNumber);
        return accountMapper.toResponse(account);

    }

    private CustomerResponseForAccount getCustomerForAccount(Long customerId) {
        try {
            return customerClient.getCustomerById(customerId);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new BusinessException("CUSTOMER_NOT_FOUND", "Customer ID: " + customerId + " not found", 400) {
                };
            } else {

                throw new RuntimeException(e);
            }
        }
    }

    private List<AccountHolder> validateAndBuildHolders(List<AccountHolderRequest> holderRequests) {

        Set<ParticipantType> holderParticipantType = new HashSet<>();
        Set<Long> customerIds = new HashSet<>();

        long primaryHolderCount = holderRequests.stream()
                .filter(holder -> holder.type() == ParticipantType.PRIMARY_HOLDER)
                .count();

        if (primaryHolderCount > 1) {
            throw new BusinessException("DUPLICATED_HOLDER_TYPE", "Account cannot have more than one primary holder", 400) {
            };
        }

        return holderRequests.stream().map(holder -> {
                    CustomerResponseForAccount customer = getCustomerForAccount(holder.customerId());

                    if (!customerIds.add(holder.customerId())) {
                        throw new BusinessException("DUPLICATED_HOLDER", "Customer cannot appear twice in the account", 400) {
                        };
                    }

                    if (!customer.status().equalsIgnoreCase("ACTIVE")) {
                        throw new InvalidCustomerException(
                                "Customer ID: " + customer.id() + " status should be active for opening account"
                        );
                    }

                    return holderMapper.toHolder(
                            holder,
                            customer
                    );
                })
                .toList();
    }

    private void validateOwnerShip(CreateAccountRequest accountRequest) {
        boolean isIndividual = accountRequest.ownershipType() == AccountOwnerShipType.INDIVIDUAL;
        boolean isJoint = accountRequest.ownershipType() == AccountOwnerShipType.JOINT;

        if (isIndividual && accountRequest.holders().size() != 1) {
            throw new InvalidHolderCountException("Individual account must have exactly one holder") {
            };
        }

        if (isJoint && accountRequest.holders().size() < 2) {
            throw new InvalidHolderCountException("Joint account must have at least two holders");
        }

        if (isJoint && accountRequest.signatureType() == SignatureType.INDIVIDUAL) {
            throw new InvalidSignatureException("Ownership account is JOINT and Signature type is INDIVIDUAL, both must match");

        }
    }

    public void activateAccount(String accountNumber) {
        Account account = new Account();
        account.activate();
    }

    public void closeAccount(String accountNumber) {
        Account account = new Account();
        account.close();
    }

    public void restrictAccount(String accountNumber) {
        Account account = new Account();
        account.restrict();
    }

    public void blockAccount(String accountNumber) {
        Account account = new Account();
        account.block();
    }

}
