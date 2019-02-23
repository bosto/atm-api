package com.bosto.atmapi.account.controller;


import com.bosto.atmapi.account.service.AccountService;
import com.bosto.atmapi.common.BaseController;
import com.bosto.atmapi.user.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class AccountController extends BaseController {
    @Autowired
    AccountService accountService;

    @GetMapping("/account")
    public Map<String, Object> get(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer) {
        return accountService.query(customer);

    }
    @PostMapping("/account")
    public Map<String, Object> post(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer) {
      return accountService.create(customer);
    }

    @PutMapping("/account/{balance}")
    public Map<String, Object> topUp(@ModelAttribute(CUSTOMER_CONTEXT) Customer customer,
                                     @PathVariable("balance") BigDecimal balance) {
      return accountService.topUp(customer, balance);
    }
}
