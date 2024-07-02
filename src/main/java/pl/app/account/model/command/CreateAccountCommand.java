package pl.app.account.model.command;

import java.math.BigDecimal;

public record CreateAccountCommand(String pesel, String name, String surname, BigDecimal balancePLN) {
    //TODO add validation
}
