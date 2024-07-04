package pl.app.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

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
    private BigDecimal balancePLN;
    private BigDecimal balanceUSD;

    public Account(String pesel, String name, String surname, BigDecimal balancePLN) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.balancePLN = balancePLN;
        this.balanceUSD = BigDecimal.ZERO;
    }

}
