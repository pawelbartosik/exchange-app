package pl.app.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.app.account.exception.AccountConflictException;
import pl.app.account.exception.AccountNotEmptyException;
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
        return accountRepository.findByPesel(pesel)
                .map(AccountDto::fromAccount)
                .orElseThrow(AccountNotFoundException::new);
    }

    public AccountDto createAccount(CreateAccountCommand command) {
        Account account = new Account(command.pesel(), command.name(), command.surname(), command.balancePLN());
        return AccountDto.fromAccount(accountRepository.save(account));
    }

    public AccountDto updateAccountData(String pesel, UpdateAccountCommand command) {
        Account account = accountRepository.findByPesel(pesel)
                .orElseThrow(AccountNotFoundException::new);
        if (!account.getPesel().equals(command.pesel())) {
            throw new AccountConflictException();
        }

        account.setName(command.name());
        account.setSurname(command.surname());
        return AccountDto.fromAccount(accountRepository.save(account));
    }

    @Transactional
    public void deleteAccount(String pesel) {
        Account account = accountRepository.findByPesel(pesel)
                .orElseThrow(AccountNotFoundException::new);

        if (account.getBalancePLN().add(account.getBalanceUSD()).compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountNotEmptyException();
        }

        accountRepository.deleteAccountByPesel(pesel);
    }
}
