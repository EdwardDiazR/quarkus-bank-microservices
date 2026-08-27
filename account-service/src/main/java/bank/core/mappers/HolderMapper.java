package bank.core.mappers;

import bank.core.entity.dto.response.CustomerResponseForAccount;
import bank.core.entity.AccountHolder;
import bank.core.entity.dto.request.AccountHolderRequest;
import bank.core.entity.dto.response.AccountHolderResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class HolderMapper {

   /* public List<AccountHolder> toHolders(List<AccountHolderRequest> holders) {
        return holders.stream().map(request->toHolder(request)).toList();
    }*/

    public AccountHolder toHolder(AccountHolderRequest holderRequest, CustomerResponseForAccount customer) {
        AccountHolder holder = new AccountHolder();
        holder.customerId = holderRequest.customerId();
        holder.participantType = holderRequest.type();
        holder.fullName = customer.fullName();
        holder.isActive = true;
        holder.nationalId = customer.nationalId();
        return holder;
    }

    public AccountHolderResponse toDto(AccountHolder accountHolder) {
        return new AccountHolderResponse(
                accountHolder.customerId,
                accountHolder.nationalId,
                accountHolder.fullName,
                accountHolder.participantType);
    }

    public List<AccountHolderResponse> toListDto(List<AccountHolder> holders) {
        return holders.stream().map(this::toDto).toList();
    }
}
