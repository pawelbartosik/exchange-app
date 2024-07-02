package pl.app.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.app.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
