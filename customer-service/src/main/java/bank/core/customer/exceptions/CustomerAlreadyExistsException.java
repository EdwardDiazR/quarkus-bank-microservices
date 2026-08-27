package bank.core.customer.exceptions;

import jakarta.ws.rs.core.Response;

public class CustomerAlreadyExistsException extends BusinessException {
    public CustomerAlreadyExistsException(String documentId) {

        super(
                "CUSTOMER_EXISTS",
                "Customer already exists with document: " + documentId,
                Response.Status.BAD_REQUEST
        );
    }
}
