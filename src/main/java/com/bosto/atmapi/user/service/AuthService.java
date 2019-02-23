package com.bosto.atmapi.user.service;

import com.bosto.atmapi.user.domain.Customer;
import com.bosto.atmapi.user.view.AddEmployeeRequest;

import java.util.Map;

public interface AuthService {
    Customer registerUser(Map<String, String> registerUser);
    Customer addEmployee(Customer org, AddEmployeeRequest request);
}
