package bank.core.exceptions;

public class InvalidHolderCountException extends BusinessException {
    public InvalidHolderCountException(String message) {
        super("INVALID_HOLDER_COUNT",message,400);
    }
}
