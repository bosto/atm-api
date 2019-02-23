package com.bosto.atmapi.config;

import com.bosto.atmapi.exception.JWTAuthenticationEntryPoint;
import com.bosto.atmapi.security.JWTAuthenticationFilter;
import com.bosto.atmapi.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Created by bosto on 2018/12/21
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	// 因为UserDetailsService的实现类实在太多啦，这里设置一下我们要注入的实现类
	@Qualifier("userDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	// 加密密码的，安全第一嘛~
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				// 测试用资源，需要验证了的用户才能访问
//				.antMatchers( "/ssh/*").authenticated()
				.antMatchers("/account","/account/**","/auth/add-employee").hasAuthority("ORG")
				.antMatchers("/atm/withdraw/approver","/atm/withdraw/approve","/atm/withdraw/reject").hasAuthority("APPROVER")
				.antMatchers("/atm/withdraw/requester","/atm/withdraw/create", "/atm/withdraw/scan").hasAuthority("REQUESTER")
				.antMatchers("/atm/withdraw/requester","/atm/withdraw/create", "/atm/withdraw/scan").authenticated()
				// 其他都放行了
				.anyRequest().permitAll()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()));
				// 不需要session
				// 加一句这个
	}

	@Bean
    CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
