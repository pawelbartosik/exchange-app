package pl.app.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.app.account.exception.AccountNotFoundException;
import pl.app.account.model.Account;
import pl.app.account.model.command.CreateAccountCommand;
import pl.app.account.model.command.UpdateAccountCommand;
import pl.app.account.model.dto.AccountDto;
import pl.app.account.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Page<AccountDto> getAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(AccountDto::fromAccount);
    }

    public AccountDto getAccount(String pesel) {
        return accountRepository.findById(pesel)
                .map(AccountDto::fromAccount)
                .orElseThrow(AccountNotFoundException::new);
    }

    public AccountDto createAccount(CreateAccountCommand command) {
        Account account = new Account(command.pesel(), command.name(), command.surname(), command.balancePLN(), BigDecimal.ZERO);
        return AccountDto.fromAccount(accountRepository.save(account));
    }

    public AccountDto updateAccountData(String pesel, UpdateAccountCommand command) {
        //TODO one query or two queries
        return null;
    }

    public void deleteAccount(String pesel) {
        // can't delete account with balance
        // soft delete maybe?
    }
}
