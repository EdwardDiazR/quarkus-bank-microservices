package bank.core.customer.dto.request;

import bank.core.customer.enums.PhoneType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerPhoneRequest(

        @NotNull(message = "Phone type is required")
        PhoneType type,

        @NotBlank(message = "Phone number is required")
        @Size(max = 30)
        String phoneNumber,

        @Size(max = 5)
        String countryCode,

        boolean primary

) {
}