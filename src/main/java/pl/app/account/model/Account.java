package pl.app.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "pesel")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String pesel;
    private String name;
    private String surname;

    private BigDecimal balancePLN;
    private BigDecimal balanceUSD;


}
