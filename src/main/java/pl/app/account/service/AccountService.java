package pl.app.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.app.account.exception.AccountAlreadyExistException;
import pl.app.account.exception.AccountConflictException;
import pl.app.account.exception.AccountNotEmptyException;
import pl.app.account.exception.AccountNotFoundException;
import pl.app.account.exception.NotEnoughMoneyException;
import pl.app.account.model.Account;
import pl.app.account.model.SubAccount;
import pl.app.account.model.command.CreateAccountCommand;
import pl.app.account.model.command.ExchangeCurrencyCommand;
import pl.app.account.model.command.UpdateAccountCommand;
import pl.app.account.model.dto.AccountDto;
import pl.app.account.model.enums.CurrencyCode;
import pl.app.account.repository.AccountRepository;
import pl.app.currency.service.CurrencyService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;

    @Transactional(readOnly = true)
    public Page<AccountDto> getAccounts(Pageable pageable) {
        return accountRepository.findAllWithSubAccounts(pageable).map(AccountDto::fromAccount);
    }

    @Transactional(readOnly = true)
    public AccountDto getAccount(String pesel) {
        return accountRepository.findByPeselWithSubAccounts(pesel)
                .map(AccountDto::fromAccount)
                .orElseThrow(AccountNotFoundException::new);
    }

    @Transactional
    public AccountDto createAccount(CreateAccountCommand command) {
        try {
            return AccountDto.fromAccount(accountRepository
                    .save(new Account(command.pesel(), command.name(), command.surname(), command.balancePLN())));
        } catch (Exception e) {
            throw new AccountAlreadyExistException();
        }
    }

    @Transactional
    public AccountDto updateAccountData(String pesel, UpdateAccountCommand command) {
        Account account = accountRepository.findByPeselWithSubAccounts(pesel)
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
        Account account = accountRepository.findByPeselWithSubAccounts(pesel)
                .orElseThrow(AccountNotFoundException::new);

        if (account.getSubAccounts()
                .stream()
                .map(SubAccount::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountNotEmptyException();
        }

        accountRepository.deleteAccountByPesel(pesel);
    }

    @Transactional
    public AccountDto exchangeCurrency(String pesel, ExchangeCurrencyCommand command) {
        Account account = accountRepository.findByPeselWithSubAccounts(pesel)
                .orElseThrow(AccountNotFoundException::new);

        SubAccount from = SubAccount.getSubAccount(account, CurrencyCode.valueOf(command.from()));
        SubAccount to = SubAccount.getSubAccount(account, CurrencyCode.valueOf(command.to()));

        if (from.getAmount().compareTo(command.amount()) < 0) {
            throw new NotEnoughMoneyException();
        }

        BigDecimal exchangedAmount = currencyService.exchangeCurrency(from.getCurrency(), to.getCurrency(), command.amount());

        from.setAmount(from.getAmount().subtract(command.amount()));
        to.setAmount(to.getAmount().add(exchangedAmount));

        return AccountDto.fromAccount(accountRepository.save(account));
    }
}
