package bank.core.customer.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCustomerRequest(

        @NotBlank(message = "Document number is required")
        @Size(max = 20, message = "Document number cannot exceed 20 characters")
        String documentNumber,

        @NotBlank(message = "Document type is required")
        @Pattern(
                regexp = "^(CEDULA|PASSPORT|RNC)$",
                message = "Allowed values: CEDULA, PASSPORT, RNC"
        )
        String documentType,

        @NotBlank(message = "Nationality is required")
        @Size(min = 2, max = 3)
        String nationality,

        @NotBlank(message = "Person type is required")
        @Pattern(
                regexp = "^(PHYSICAL|JURIDICAL)$",
                message = "Allowed values: PHYSICAL, JURIDICAL"
        )
        String personType,

        @NotBlank(message = "First name is required")
        @Size(max = 50)
        String firstName,

        @Size(max = 50)
        String middleName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50)
        String lastName,

        @Size(max = 50)
        String secondLastName,

        @NotBlank(message = "Gender is required")
        @Pattern(
                regexp = "^[MF]$",
                message = "Gender must be M or F"
        )
        String gender,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        boolean isPep,

        BigDecimal monthlyIncome,

        @Valid
        @NotNull(message = "Contacts are required")
        CustomerContactsRequest contacts
) {
}