package pl.app.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.app.account.model.SubAccount;

import java.math.BigDecimal;

public interface SubAccountRepository extends JpaRepository<SubAccount, Integer> {
}
