package bank.core.customer.dto.response;

import bank.core.customer.enums.CustomerStatus;

public record CustomerAccountValidationResponse(
        Long id,
        String nationalId,
        String fullName,
        CustomerStatus status
) {
}
