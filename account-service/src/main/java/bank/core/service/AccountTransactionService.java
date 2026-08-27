package bank.core.service;

import bank.core.entity.Account;
import bank.core.entity.Money;
import bank.core.entity.dto.request.WithdrawRequest;
import bank.core.entity.dto.response.CashDepositResponse;
import bank.core.exceptions.AccountNotFoundException;
import bank.core.repository.AccountRepository;
import io.quarkus.arc.Lock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class AccountTransactionService {
    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountService accountService;


    @Transactional()
    public Money withdraw(WithdrawRequest request) {
        Account account = accountRepository.findById(request.accountId());
        Money response = account.withdraw(request.amount());
        accountRepository.persist(account);
        return response;
    }

    @Transactional
    public CashDepositResponse deposit(Long accountId, Money amount, String description) {
        Account account = accountRepository.findByIdOptional(accountId)
                .orElseThrow(() -> new AccountNotFoundException("ACCOUNT NOT FOUND"));
        account.deposit(amount);
        accountRepository.persist(account);
        String message = !description.isBlank() ? description : "DEPOSITO";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");
        return new CashDepositResponse(amount, message, LocalDateTime.now().format(formatter));
    }
}
