package com.bosto.atmapi.account.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;

@Data
@Entity
public class Account {
    @Id
    @SequenceGenerator(name = "account_generator", sequenceName = "account_seq")
    @GeneratedValue(generator = "account_generator")
    private Long Id;
    String accountNumber;
    BigDecimal balance;
    Currency currency;
    long lastModifiedDatetime;

    public enum Currency {
      HKD,
      CNY,
      USD;
    }
}
