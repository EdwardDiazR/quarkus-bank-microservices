package bank.core.customer.exceptions;

import jakarta.ws.rs.core.Response;

public class CustomerNotFoundException extends BusinessException {

    public CustomerNotFoundException(String id) {

        super(
                "CUSTOMER_NOT_FOUND",
                "Customer not found id: " + id,
                Response.Status.NOT_FOUND
        );
    }
}
