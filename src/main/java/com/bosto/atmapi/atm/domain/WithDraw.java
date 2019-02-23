package com.bosto.atmapi.atm.domain;

import com.bosto.atmapi.account.domain.Account;
import com.bosto.atmapi.user.domain.Customer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
public class WithDraw {

    @Id
    @SequenceGenerator(name = "withdraw_generator", sequenceName = "withdraw_seq")
    @GeneratedValue(generator = "withdraw_generator")
//    @JsonIgnore
    Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @NotNull
    Customer requester;

    @ManyToOne(cascade = CascadeType.REFRESH)
    Customer lastOperator;

    String accountNumber;

    String purpose;

    BigDecimal amount;

    Account.Currency currency;

    Status status;

    String comment;

    long lastModifiedDatetime;

    long createdDatetime;

    public enum Status {
        TAKEN,
        PENDING,
        APPROVED,
        REJECTED;
    }


}
