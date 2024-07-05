package pl.app.account.model.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record ExchangeCurrencyCommand(@Pattern(regexp = "\\d{11}", message = "pesel must be valid") String pesel,
                                      @NotEmpty(message = "currency from cannot be empty") String from,
                                      @NotEmpty(message = "currency to cannot be empty") String to,
                                      @DecimalMin(value = "0", inclusive = false, message = "amount must be greater than 0") double amount) {
}
