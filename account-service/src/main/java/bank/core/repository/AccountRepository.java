package bank.core.repository;

import bank.core.entity.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public Optional<Account> findByIdWithHolders(Long id) {

        return find("""
                SELECT DISTINCT a
                FROM Account a
                LEFT JOIN FETCH a.holders
                WHERE a.id = ?1
                """, id)
                .firstResultOptional();
    }

    public Optional<Account> findByPublicNumber(String number){
        return find("publicNumber",number).firstResultOptional();
    }
}
