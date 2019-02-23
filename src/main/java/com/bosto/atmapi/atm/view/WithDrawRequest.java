package com.bosto.atmapi.atm.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithDrawRequest {
    String purpose;
    BigDecimal amount;
}
