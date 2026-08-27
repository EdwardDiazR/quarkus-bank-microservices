package bank.core.customer.exceptions;

import jakarta.ws.rs.core.Response;

public class BusinessException extends RuntimeException {

    private final String code;
    private final Response.Status status;

    public BusinessException(
            String code,
            String message,
            Response.Status status) {

        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public Response.Status getStatus() {
        return status;
    }
}