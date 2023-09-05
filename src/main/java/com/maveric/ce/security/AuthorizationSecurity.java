package com.maveric.ce.security;

import com.maveric.ce.dto.UserDetailsImpl;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.serviceImpl.LoginServiceImpl;
import com.maveric.ce.utils.JWTUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class AuthorizationSecurity extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationSecurity.class);
	@Autowired
	public LoginServiceImpl loginImpl;
	@Autowired
	JWTUtils jwtUtils;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServiceException, java.io.IOException, ServletException {
		String uri = request.getRequestURI();
		logger.info("uri:"+uri);
		if (uri.startsWith("/login") || uri.equals("/authenticate/login") || uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			String token = request.getHeader("Authorization");
			if (StringUtils.isNotEmpty(token)) {
				String userEmail = JWTUtils.extractUserMailId(token);
				if (StringUtils.isNotEmpty(userEmail)) {
					UserDetailsImpl userDetails = this.loginImpl.loadUserByUsername(userEmail);
					if (jwtUtils.isTokenValid(token, userDetails)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
								null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authToken);
						response.setHeader("Authorization", token);
						filterChain.doFilter(request, response);

					} else {
						throw new ServiceException(ErrorCodes.INVALID_TOKEN);
					}
				} else {
					throw new ServiceException(ErrorCodes.INVALID_TOKEN);
				}
			}else{
				throw new ServiceException(ErrorCodes.PAGE_NOT_FOUND);
			}
		} catch (ServiceException e) {
			resolver.resolveException(request, response, null, e);
		}catch (Exception ex){
			ex.getStackTrace();
		}
	}
}