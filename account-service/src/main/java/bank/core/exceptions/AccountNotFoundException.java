package bank.core.exceptions;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(String message) {

        super("ACCOUNT_NOT_FOUND", message, 400);
    }
}
