package com.bosto.atmapi.user.controller;


import com.bosto.atmapi.common.BaseController;
import com.bosto.atmapi.user.domain.Customer;
import com.bosto.atmapi.user.service.AuthService;
import com.bosto.atmapi.user.view.AddEmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;



/**
 * Created by bosto on 2018/12/21
 **/
@RestController
@RequestMapping("/auth")
public class CustomerController extends BaseController {

	@Autowired
	AuthService authService;

	@PostMapping("/register")
	public Customer registerUser(@RequestBody Map<String,String> registerUser){
		return authService.registerUser(registerUser);
	}

	@PostMapping("/add-employee")
	public Customer addEmployee(@ModelAttribute(CUSTOMER_CONTEXT) Customer org,
								@Valid @RequestBody AddEmployeeRequest request){
		return authService.addEmployee(org, request);
	}


}
