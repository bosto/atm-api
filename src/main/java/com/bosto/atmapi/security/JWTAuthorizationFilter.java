package com.bosto.atmapi.security;


import com.bosto.atmapi.exception.JWTAuthenticationEntryPoint;
import com.bosto.atmapi.user.domain.Customer;
import com.bosto.atmapi.user.domain.JwtUser;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bosto on 2018/12/21
 **/
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private JWTAuthenticationEntryPoint entryPoint = new JWTAuthenticationEntryPoint();

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

		String tokenHeader = request.getHeader(JwtToken.TOKEN_HEADER);
		// 如果请求头中没有Authorization信息则直接放行了
		if (tokenHeader == null || !tokenHeader.startsWith(JwtToken.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		// 如果请求头中有token，则进行解析，并且设置认证信息
		try {
			UsernamePasswordAuthenticationToken authenticationToken = null;
			String token = tokenHeader.replace(JwtToken.TOKEN_PREFIX, "");
			String username = JwtToken.getUsername(token);
			String role = JwtToken.getRoleClaims(token);
			Customer userBean = new Customer();
			userBean.setUsername(username);
			userBean.setRole(role);
			JwtUser jwtUser = new JwtUser(userBean);


			if (username != null){
				authenticationToken =  new UsernamePasswordAuthenticationToken(username, null,
						jwtUser.getAuthorities());
			}
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			String renewToken =
					JwtToken.createToken(username, role, false);
			request.setAttribute("CustomerContext", userBean);
			response.setHeader(JwtToken.RESPONSE_TOKEN, renewToken);
			response.setHeader("Access-Control-Expose-Headers", JwtToken.RESPONSE_TOKEN);
		} catch (AuthenticationException authException) {
			entryPoint.commence(request, response, authException);
			return;
		}
		super.doFilterInternal(request, response, chain);
	}

	// 这里从token中获取用户信息并新建一个token
//	private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
//		String token = tokenHeader.replace(JwtToken.TOKEN_PREFIX, "");
//		String username = JwtToken.getUsername(token);
//		String role = JwtToken.getRoleClaims(token);
//		if (username != null){
//			return new UsernamePasswordAuthenticationToken(username, null,
//					Collections.singleton(new SimpleGrantedAuthority(role)));
//		}
//		return null;
//	}
}
