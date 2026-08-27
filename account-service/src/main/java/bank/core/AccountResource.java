package bank.core;

import bank.core.clients.CustomerClient;
import bank.core.entity.Money;
import bank.core.entity.dto.response.AccountResponse;
import bank.core.entity.dto.request.CreateAccountRequest;
import bank.core.entity.dto.request.DepositRequest;
import bank.core.entity.dto.request.WithdrawRequest;
import bank.core.entity.dto.response.CashDepositResponse;
import bank.core.service.AccountService;
import bank.core.service.AccountTransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.temporal.ChronoUnit;

@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Timeout(3000)
@Retry(maxRetries = 2,delay = 500,delayUnit = ChronoUnit.MILLIS)
public class AccountResource {

    @Inject
    @RestClient
    CustomerClient customerClient;

    @Inject
    AccountService accountService;

    @Inject
    AccountTransactionService accountTransactionService;

    @GET
    public Object hello() {
        return customerClient.getCustomerById(1L);
    }

    @GET
    @Path("{id}")
    public AccountResponse getById(@PathParam("id") Long id) {
        return accountService.getById(id);
    }

    @GET
    @Path("/number/{number}")
    public AccountResponse getByNumber(@PathParam("number") String accountNumber) {
        return accountService.getByNumber(accountNumber);
    }

    @POST
    @Path("withdraw")
    public Money withdraw(WithdrawRequest request) {
        return accountTransactionService.withdraw(request);
    }

    @POST
    @Path("")
    public AccountResponse open(CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @POST
    @Path("deposit/cash")
    public Response depositCash(DepositRequest request) {
        CashDepositResponse cashDepositResponse = accountTransactionService
                .deposit(request.accountId(), request.amount(),request.description());
        return Response.ok().entity(cashDepositResponse).build();
    }
}
