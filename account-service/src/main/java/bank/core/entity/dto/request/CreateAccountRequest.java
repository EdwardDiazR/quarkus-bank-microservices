package bank.core.entity.dto.request;

import bank.core.enums.AccountOwnerShipType;
import bank.core.enums.AccountType;
import bank.core.enums.Currency;
import bank.core.enums.SignatureType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateAccountRequest(
        @NotBlank
        int productCode,

        @NotBlank
        Currency currencyCode,

        AccountType type,

        @NotNull
        AccountOwnerShipType ownershipType,

        @NotNull
        SignatureType signatureType,

        @NotEmpty
        List<@Valid AccountHolderRequest> holders,

        @NotBlank
        String branchCode

) {
}
