package com.bosto.atmapi.account.dao;

import com.bosto.atmapi.account.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface AccountRepository extends CrudRepository<Account, Long> {
  boolean existsAccountByAccountNumberEqualsAndCurrencyEqualsAndBalanceGreaterThan(
    String accountNumber, Account.Currency currency, BigDecimal amount) ;
  Account findByAccountNumber(String accountNumber);

  boolean existsAccountByAccountNumberEqualsAndCurrencyEquals(String accountNumber, Account.Currency currency);
  Account findAccountByAccountNumberEqualsAndCurrencyEquals(String accountNumber, Account.Currency currency);

}
