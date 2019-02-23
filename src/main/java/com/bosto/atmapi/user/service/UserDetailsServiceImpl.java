package com.bosto.atmapi.user.service;


import com.bosto.atmapi.user.dao.UserRepository;
import com.bosto.atmapi.user.domain.Customer;
import com.bosto.atmapi.user.domain.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by bosto on 2018/12/21
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer user = userRepository.findByUsername(username);

		return new JwtUser(user);
	}

}
