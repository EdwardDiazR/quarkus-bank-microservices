package bank.core.entity;

import bank.core.enums.Currency;

import java.math.BigDecimal;

public class Money {
    public BigDecimal amount;
    public Currency currency;

    public Money(BigDecimal amount, Currency currency){
        this.amount = amount;
        this.currency = currency;
    }
}
