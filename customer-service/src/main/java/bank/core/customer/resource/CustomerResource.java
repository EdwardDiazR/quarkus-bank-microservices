package bank.core.customer.resource;

import bank.core.customer.dto.request.CreateCustomerRequest;
import bank.core.customer.dto.response.CustomerAccountValidationResponse;
import bank.core.customer.entity.Customer;
import bank.core.customer.enums.CustomerStatus;
import bank.core.customer.interfaces.ICustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("api/v1/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    ICustomerService customerService;

    @POST
    public Response create(@Valid CreateCustomerRequest customer) {
        Customer customerResponse = customerService.create(customer);
        return Response.created(URI.create("")).entity(customerResponse).build();
    }

    @GET
    @Path("{id}")
    public Response getCustomerById(@PathParam("id") Long id)  {
        return Response.ok(customerService.findById(id)).build();
    }

    @GET
    @Path("document/{type}/{number}")
    public Response getCustomerByDocumentNumber(@PathParam("number") String number,
                                                @PathParam("type") String type) {
        return Response.ok(customerService.findByDocumentNumber(number)).build();
    }

    @PUT
    public Response update(Customer customerUpdated) {
        customerService.update(customerUpdated.id, customerUpdated);
        return Response.ok("Actualizado con exito").build();
    }

    @GET
    @Path("{id}/status")
    public String getStatusByCustomerId(@PathParam("id") Long customerId) throws InterruptedException {
        /*Thread.sleep(10000);*/

        return customerService.getStatusByCustomerId(customerId).toString();
    }

    @PATCH
    @Path("{id}/status")
    public Response updateCustomerStatus(@PathParam("id") Long customerId,
                                         @QueryParam("newStatus") CustomerStatus newStatus) {

        customerService.updateCustomerStatus(customerId, newStatus);

        return Response.ok("Status update successful").build();
    }


    @GET
    @Path("/{customerId}/account-validation")
    public CustomerAccountValidationResponse validateForAccount(
            @PathParam("customerId") Long customerId) {

        return customerService.getCustomerForAccount(customerId);
    }

    @GET
    @Path("{id}/risk-level")
    public Response getCustomerRiskLevel(@PathParam("id") Long customerId) {
        String riskLevel = customerService.getRiskLevelByCustomerId(customerId);

        return Response.ok(riskLevel).build();
    }

    @PATCH
    @Path("{id}/risk-level")
    public Response updateCustomerRiskLevel(@PathParam("id") Long customerId,
                                         @QueryParam("newLevel") int newLevel) {
        customerService.updateCustomerRiskLevel(customerId, newLevel);

        return Response.ok("Status update successful").build();
    }
}
