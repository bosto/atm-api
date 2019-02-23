package com.bosto.atmapi.user.dao;

import com.bosto.atmapi.user.domain.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by bosto on 2018/12/21
 **/
public interface UserRepository extends CrudRepository<Customer, Integer> {
    Customer findByUsername(String username);
}
