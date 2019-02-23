package com.bosto.atmapi.user.view;

import com.bosto.atmapi.user.domain.Customer;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddEmployeeRequest {
    @NotNull
    String employeeName;

    @NotNull
    Customer.Role role;
}
