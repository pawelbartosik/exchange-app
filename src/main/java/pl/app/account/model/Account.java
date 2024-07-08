package pl.app.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.app.account.model.enums.CurrencyCode;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "pesel")
@ToString
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id = ?")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String pesel;
    private String name;
    private String surname;
    private boolean deleted;

    @OneToMany(mappedBy = "account")
    private Set<SubAccount> subAccounts = new HashSet<>();

    public Account(String pesel, String name, String surname, BigDecimal balancePLN) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;

    new SubAccount(this, CurrencyCode.PLN, new BigDecimal(String.valueOf(balancePLN)));
        new SubAccount(this, CurrencyCode.USD, BigDecimal.ZERO);
    }

}
