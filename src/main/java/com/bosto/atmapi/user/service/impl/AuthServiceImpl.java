package com.bosto.atmapi.user.service.impl;

import com.bosto.atmapi.exception.BaseException;
import com.bosto.atmapi.user.dao.UserRepository;
import com.bosto.atmapi.user.domain.Customer;
import com.bosto.atmapi.user.service.AuthService;
import com.bosto.atmapi.user.view.AddEmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bosto.atmapi.exception.ErrorCodes.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public Customer registerUser(Map<String,String> registerUser) {
        Customer user = new Customer();
        if (userRepository.findByUsername(registerUser.get("username")) != null) {
            throw new BaseException(USER_NAME_DUPLICATE,
                    "User name "
                            + registerUser.get("username")
                            + " is exist! Please use another name");
        }
        user.setUsername(registerUser.get("username"));
        user.setReferenceNumber(registerUser.get("referenceNumber"));
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        user.setRole(registerUser.getOrDefault("role", "ROLE_USER"));
        userRepository.save(user);
        return user;
    }

    public Customer addEmployee(Customer org, AddEmployeeRequest request) {
        Customer customer = userRepository.findByUsername(request.getEmployeeName());
        customer.setOrganization(org);
        customer.setRole(request.getRole().toString());
        userRepository.save(customer);
        return customer;
    }

}
