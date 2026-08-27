package bank.core.exceptions;

public class InvalidCustomerException extends BusinessException {
    public InvalidCustomerException(String message) {
        super("CUSTOMER_NOT_ACTIVE", message, 400);
    }
}
