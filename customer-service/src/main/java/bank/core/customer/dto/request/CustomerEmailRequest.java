package bank.core.customer.dto.request;

import bank.core.customer.enums.EmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerEmailRequest(

        @NotNull(message = "Email type is required")
        EmailType type,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 150)
        String email,

        boolean primary

) {
}