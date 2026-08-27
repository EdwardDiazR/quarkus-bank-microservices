package bank.core.exceptions;

public class InvalidSignatureException extends BusinessException {
    public InvalidSignatureException(String message) {
        super("INVALID_SIGNATURE", message, 400);
    }
}
