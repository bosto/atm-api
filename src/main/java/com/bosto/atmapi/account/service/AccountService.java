package com.bosto.atmapi.account.service;

import com.bosto.atmapi.account.dao.AccountRepository;
import com.bosto.atmapi.account.domain.Account;
import com.bosto.atmapi.exception.BaseException;
import com.bosto.atmapi.user.dao.UserRepository;
import com.bosto.atmapi.user.domain.Customer;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.bosto.atmapi.common.Maps.*;
import static com.bosto.atmapi.common.RandomUtils.*;

@Service
@Slf4j
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;
    public Map<String, Object> withDraw(Customer customer, BigDecimal amount) {
        log.info("withDraw---------------1");
        Account account;
        if (customer.getOrganization() != null) {
            account = customer.getOrganization().getAccount();
        } else {
            account = customer.getAccount();
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BaseException("400", "Not enough balance to withdraw");
        }

        log.info("withDraw---------------2");
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        account.setLastModifiedDatetime(System.currentTimeMillis());
        accountRepository.save(account);
        return map("success", true);
    }

    public Map<String, Object> topUp(Customer customer, BigDecimal amount) {
        Account account = customer.getAccount();
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        account.setLastModifiedDatetime(System.currentTimeMillis());
        accountRepository.save(account);
        return map("success", true);
    }

    public Map<String, Object> create(Customer customer) {
        if (customer.getAccount() != null) {
            throw new BaseException("400","Already have account, can't create more");
        }
        Account account = new Account();
        String accountNumber = accountNumber();
        account.setAccountNumber(accountNumber());
        account.setCurrency(Account.Currency.HKD);
        account.setBalance(new BigDecimal(99999));
        if (accountRepository.existsAccountByAccountNumberEqualsAndCurrencyEquals(accountNumber, Account.Currency.HKD)) {
            throw new BaseException("400", "Account exist");
        }
        account.setLastModifiedDatetime(System.currentTimeMillis());
        Account saveAccount = accountRepository.save(account);
        customer.setAccount(saveAccount);
        userRepository.save(customer);

        return map("success", true);
    }

    public Map<String, Object> query(Customer customer) {
      return map("account", customer.getAccount());
    }

    public boolean checkBalance(String accountNumber, Account.Currency currency,
                                            BigDecimal amount) {
        return accountRepository
          .existsAccountByAccountNumberEqualsAndCurrencyEqualsAndBalanceGreaterThan(accountNumber, currency, amount);
    }
}
