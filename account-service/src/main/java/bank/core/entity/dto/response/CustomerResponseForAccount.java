package bank.core.entity.dto.response;

public record CustomerResponseForAccount(Long id,
                               String nationalId,
                               String fullName,
                               String status) {
}
