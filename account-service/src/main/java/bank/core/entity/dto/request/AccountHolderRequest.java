package bank.core.entity.dto.request;

import bank.core.enums.ParticipantType;

public record AccountHolderRequest(
        Long customerId,
        ParticipantType type
) {
}
