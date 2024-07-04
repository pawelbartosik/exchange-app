package pl.app.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.app.account.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByPesel(String pesel);

    @Modifying
    @Query("update Account a set a.deleted = true where a.pesel = :pesel")
    void deleteAccountByPesel(String pesel);
}
