package bank.core.clients;

import bank.core.entity.dto.response.CustomerResponseForAccount;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "customer-service")
@Path("api/v1/customer")
@Produces(MediaType.APPLICATION_JSON)
/*@Timeout(2000)
@Retry(maxRetries = 2,delay = 500 ,durationUnit = ChronoUnit.MILLIS)*/
public interface CustomerClient {

    @GET
    @Path("/{customerId}/account-validation")
    CustomerResponseForAccount getCustomerById(@PathParam("customerId") Long customerId);

    @GET
    @Path("{customerId}/status")
    String getCustomerStatus(@PathParam("customerId") Long customerId);
}
