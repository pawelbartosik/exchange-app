package pl.app.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.app.account.exception.AccountAlreadyExistException;
import pl.app.account.exception.AccountConflictException;
import pl.app.account.exception.AccountNotFoundException;
import pl.app.account.model.Account;
import pl.app.account.model.command.CreateAccountCommand;
import pl.app.account.model.command.UpdateAccountCommand;
import pl.app.account.model.dto.AccountDto;
import pl.app.account.repository.AccountRepository;
import pl.app.account.repository.SubAccountRepository;
import pl.app.currency.service.CurrencyService;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final SubAccountRepository subAccountRepository;
    private final CurrencyService currencyService;

    public Page<AccountDto> getAccounts(Pageable pageable) {
        return accountRepository.findAllWithSubAccounts(pageable).map(AccountDto::fromAccount);
    }

    public AccountDto getAccount(String pesel) {
        return accountRepository.findByPeselWithSubAccounts(pesel)
                .map(AccountDto::fromAccount)
                .orElseThrow(AccountNotFoundException::new);
    }

    @Transactional
    public AccountDto createAccount(CreateAccountCommand command) {
        if (accountRepository.restoreDeletedAccount(command.pesel(), command.name(), command.surname()) &&
                subAccountRepository.restoreDeletedSubAccounts(command.pesel(), command.balancePLN()) > 0) {
            return getAccount(command.pesel());
        }

        try {
            return AccountDto.fromAccount(accountRepository
                    .save(new Account(command.pesel(), command.name(), command.surname(), command.balancePLN())));
        } catch (Exception e) {
            throw new AccountAlreadyExistException();
        }
    }

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

//    @Transactional
//    public void deleteAccount(String pesel) {
//        Account account = accountRepository.findByPesel(pesel)
//                .orElseThrow(AccountNotFoundException::new);
//
//        if (account.getBalancePLN().add(account.getBalanceUSD()).compareTo(BigDecimal.ZERO) > 0) {
//            throw new AccountNotEmptyException();
//        }
//
//        accountRepository.deleteAccountByPesel(pesel);
//    }

//    @Transactional
//    public AccountDto exchangeCurrency(String pesel, ExchangeCurrencyCommand command) {
//        //if account not exist throw exception
//        Account account = accountRepository.findByPesel(pesel)
//                .orElseThrow(AccountNotFoundException::new);
//        //check balance
//        if (account)
//        //accountService.validateAccountForExchange(pesel, command);
//
//        //get rate - currencyService.getRate()
//        //everything should be inside this method
//
//        //calculate amount or throw exception
//        //update account balance
//    }
}
