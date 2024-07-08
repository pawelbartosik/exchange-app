package pl.app.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.app.account.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
//    Optional<Account> findByPesel(String pesel);

    @Modifying
    @Query("update Account a set a.deleted = true where a.pesel = :pesel")
    void deleteAccountByPesel(String pesel);

    @Modifying
    @Query(nativeQuery = true,
            value = "update Account a set a.deleted = false, a.name = :name, a.surname = :surname" +
                    " where a.pesel = :pesel and a.deleted = true")
    int restoreDeletedAccount(String pesel, String name, String surname);

    @Query("select a from Account a left join fetch a.subAccounts where a.pesel = :pesel")
    Optional<Account> findByPeselWithSubAccounts(String pesel);

    @Query("select a from Account a left join fetch a.subAccounts")
    Page<Account> findAllWithSubAccounts(Pageable pageable);

//    boolean existsByPesel(String pesel);
}
