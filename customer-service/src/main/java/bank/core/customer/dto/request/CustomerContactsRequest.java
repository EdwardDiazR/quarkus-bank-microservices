package bank.core.customer.dto.request;

import jakarta.validation.Valid;

import java.util.List;

public record CustomerContactsRequest(

        @Valid
        List<CustomerAddressRequest> addresses,

        @Valid
        List<CustomerPhoneRequest> phones,

        @Valid
        List<CustomerEmailRequest> emails

) {
}