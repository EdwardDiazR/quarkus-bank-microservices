package bank.core.entity.dto.request;

import bank.core.entity.Money;

public record WithdrawRequest(Long accountId, Money amount) {
}
