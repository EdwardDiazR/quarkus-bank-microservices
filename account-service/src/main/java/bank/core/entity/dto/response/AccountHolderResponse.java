package bank.core.entity.dto.response;

import bank.core.enums.ParticipantType;

public record AccountHolderResponse(
        Long customerId,
        String nationalId,
        String fullName,
        ParticipantType participantType
) {
}
