package bank.core.entity.dto.request;

import bank.core.entity.Money;

public record  DepositRequest(Long accountId,
                              Money amount,
                              String description,
                              String channel) {
}
