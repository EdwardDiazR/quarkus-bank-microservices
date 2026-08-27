package bank.core.entity.dto.response;

import bank.core.entity.Money;
import bank.core.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashDepositResponse(Money amount,
                                  String message,
                                  String date) {
}
