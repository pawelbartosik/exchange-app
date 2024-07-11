package pl.app.account.model.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import pl.app.validation.annotation.ValidPeselAge;

import java.math.BigDecimal;

public record CreateAccountCommand(@Pattern(regexp = "\\d{11}", message = "PESEL_MUST_BE_VALID")
                                   @ValidPeselAge String pesel,
                                   @NotEmpty(message = "NAME_CANNOT_BE_EMPTY") String name,
                                   @NotEmpty(message = "SURNAME_CANNOT_BE_EMPTY") String surname,
                                   @DecimalMin(value = "0", message = "BALANCE_CANNOT_BE_NEGATIVE") BigDecimal balancePLN) {
}
