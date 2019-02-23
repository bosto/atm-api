package com.bosto.atmapi.security;

import com.bosto.atmapi.exception.ErrorCodes;
import com.bosto.atmapi.user.domain.Customer;
import com.bosto.atmapi.user.domain.JwtUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bosto.atmapi.common.Maps.map;

/**
 * Created by bosto on 2018/12/21
 **/
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private ObjectMapper objectMapper = new ObjectMapper();

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		super.setFilterProcessesUrl("/auth/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

		// 从输入流中获取到登录的信息
		try {
			Customer loginUser = objectMapper.readValue(request.getInputStream(), Customer.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>())
			);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 成功验证后调用的方法
	// 如果验证成功，就生成token并返回
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
		// 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
		// 所以就是JwtUser啦
		JwtUser user = (JwtUser) authResult.getPrincipal();
		logger.debug("jwtUser:" + user.toString());
//		String role = "";
		// 因为在JwtUser中存了权限信息，可以直接获取，由于只有一个角色就这么干了
//		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		String token = JwtToken.createToken(user.getUsername(), user.getRoles(), false);
		// 返回创建成功的token
		// 但是这里创建的token只是单纯的token
		// 按照jwt的规定，最后请求的格式应该是 `Bearer token`
		response.setHeader(JwtToken.RESPONSE_TOKEN, token);
		Map<String, Object> result = new HashMap<>();
		result.put("username",user.getUsername());
		result.put("token", token);
		response.getWriter().write(objectMapper.writeValueAsString(result));
		response.setContentType("application/json");

	}

	// 这是验证失败时候调用的方法
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.getWriter().write( objectMapper
				.writeValueAsString(map(ErrorCodes.ERROR_MESSAGE_KEY,
						"authentication failed, reason: " + failed.getMessage())));
		response.setStatus(403);
		response.setContentType("application/json");
	}
}
