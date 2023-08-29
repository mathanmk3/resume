package com.maveric.ce.response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoginResponse {

	private String accessToken;
	private String refreshToken;

}
