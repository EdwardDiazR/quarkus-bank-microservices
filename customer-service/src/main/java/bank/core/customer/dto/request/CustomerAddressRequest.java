package bank.core.customer.dto.request;

import bank.core.customer.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerAddressRequest(

        @NotNull(message = "Address type is required")
        AddressType type,

        @NotBlank(message = "Street is required")
        @Size(max = 200)
        String street,

        @NotBlank(message = "Sector is required")
        @Size(max = 100)
        String sector,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "Province is required")
        @Size(max = 100)
        String province,

        @Size(max = 20)
        String postalCode,

        @Size(max = 250)
        String reference,

        boolean primary

) {
}