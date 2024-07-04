package pl.app.account.model.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CreateAccountCommand(@Pattern(regexp = "\\d{11}", message = "pesel must be valid") String pesel,
                                   @NotEmpty(message = "name cannot be empty") String name,
                                   @NotEmpty(message = "surname cannot be empty") String surname,
                                   @DecimalMin(value = "0", message = "balance cannot be lower than zero") BigDecimal balancePLN) {
    //TODO add validation
}
