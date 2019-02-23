package com.bosto.atmapi.common;


import com.bosto.atmapi.user.dao.UserRepository;
import com.bosto.atmapi.user.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    public static final String CUSTOMER_CONTEXT = "CustomerContext";

    @Autowired
    protected UserRepository userRepository;

    @ModelAttribute(CUSTOMER_CONTEXT)
    public Customer getCustomer(final HttpServletRequest request) {
        Customer customer = null;
        if (request.getAttribute(CUSTOMER_CONTEXT) != null) {
            customer = (Customer) request.getAttribute(CUSTOMER_CONTEXT);
            customer = userRepository.findByUsername(customer.getUsername());
        }

        return customer;
    }
}
