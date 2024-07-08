package pl.app.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.app.account.model.enums.CurrencyCode;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"account", "currency"})
@ToString(exclude = "account")
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE sub_account SET deleted = true WHERE id = ?")
public class SubAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pesel", referencedColumnName = "pesel", columnDefinition = "VARCHAR(11)")
    private Account account;
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;
    private BigDecimal amount;
    private boolean deleted;

    public SubAccount(Account account, CurrencyCode currency, BigDecimal amount) {
        this.account = account;
        this.currency = currency;
        this.amount = amount;

        this.account.getSubAccounts().add(this);
    }

}
