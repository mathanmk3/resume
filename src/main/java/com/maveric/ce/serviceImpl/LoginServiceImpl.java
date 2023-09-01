package com.maveric.ce.serviceImpl;

import com.maveric.ce.dto.LoginDto;
import com.maveric.ce.dto.UserDetailsImpl;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.response.LoginResponse;
import com.maveric.ce.service.LoginService;
import com.maveric.ce.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class LoginServiceImpl implements UserDetailsService, LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	ICustomerRepository customerRepo;
	@Autowired
	LoginResponse loginResponse;
	@Autowired
	JWTUtils jwtUtils;

	@Autowired
	PasswordEncoder passwordEncoder;
	

	public LoginResponse findUserByMailId(LoginDto loginDto, HttpServletRequest request) throws ServiceException {
		Optional<CustomerDetails> userDetails = customerRepo.findByEmail(loginDto.getEmail());
		UserDetailsImpl dto = new UserDetailsImpl();
		if (userDetails.isPresent()) {

			logger.info("original password inputted:"+loginDto.getPassword());
			logger.info("Encoded original password:"+passwordEncoder.encode(loginDto.getPassword()));
			logger.info("Encoded db password:"+(userDetails.get().getPassword()));
			if(passwordEncoder.matches(loginDto.getPassword(),userDetails.get().getPassword())) {

				dto.setUsername(userDetails.get().getUsername());
				dto.setPassword(userDetails.get().getPassword());
				dto.setEmailId(userDetails.get().getEmail());
				String role = userDetails.get().getRolesENum().toString();
				List<SimpleGrantedAuthority> authorities = new LinkedList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
				dto.setAuthorities(authorities);
				jwtUtils.generateToken(dto, request, loginResponse);
			}
			else{
				logger.info("Else:Invalid credentials");
				throw new ServiceException(ErrorCodes.INVALID_AUTHENTICATION);
			}

		}else{
			logger.info("Else:Invalid credentials");
			throw new ServiceException(ErrorCodes.INVALID_AUTHENTICATION);
		}
		return loginResponse;
  
	}

	@Override
	public UserDetailsImpl loadUserByUsername(String mailId) throws UsernameNotFoundException {
		Optional<CustomerDetails> userDetails = customerRepo.findByEmail(mailId);
		UserDetailsImpl dto = new UserDetailsImpl();
		if (userDetails.isPresent()) {
			dto.setUsername(userDetails.get().getUsername());
			dto.setPassword(userDetails.get().getPassword());
			dto.setEmailId(userDetails.get().getEmail());
			String role = userDetails.get().getRolesENum().toString();
			List<SimpleGrantedAuthority> authorities = new LinkedList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			dto.setAuthorities(authorities);
		} else {
			throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
		}
		return dto;
	}
	
	

}
