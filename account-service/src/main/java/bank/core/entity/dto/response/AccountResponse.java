package bank.core.entity.dto.response;

import bank.core.enums.AccountOwnerShipType;
import bank.core.enums.AccountStatus;
import bank.core.enums.Currency;
import bank.core.enums.SignatureType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.List;


@JsonPropertyOrder({
        "id",
        "publicNumber",
        "currency",
        "status",
        "ownerShipType",
        "signatureType",
        "availableBalance",
        "holders"
})
public record AccountResponse(
        Long id,
        String publicNumber,
        Currency currency,
        AccountStatus status,
        AccountOwnerShipType ownerShipType,
        SignatureType signatureType,
        BigDecimal availableBalance,
        List<AccountHolderResponse> holders
) {
}
