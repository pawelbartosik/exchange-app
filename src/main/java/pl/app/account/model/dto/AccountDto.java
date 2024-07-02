package pl.app.account.model.dto;

import pl.app.account.model.Account;

import java.math.BigDecimal;

public record AccountDto(String pesel, String name, String surname, BigDecimal balancePLN, BigDecimal balanceUSD) {

    public static AccountDto fromAccount(Account account) {
        return new AccountDto(account.getPesel(), account.getName(), account.getSurname(), account.getBalancePLN(), account.getBalanceUSD());
    }
}
