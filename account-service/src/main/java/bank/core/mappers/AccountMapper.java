package bank.core.mappers;

import bank.core.entity.Account;
import bank.core.entity.dto.response.AccountHolderResponse;
import bank.core.entity.dto.response.AccountResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AccountMapper {

    @Inject
    HolderMapper holderMapper;

    public AccountResponse toResponse(Account account) {
        List<AccountHolderResponse> holderDTOS = holderMapper.toListDto(account.holders);

        return new AccountResponse(
                account.id,
                account.publicNumber,
                account.currency,
                account.status,
                account.ownerShipType,
                account.signatureType,
                account.availableBalance,
                holderDTOS
        );
    }
}
