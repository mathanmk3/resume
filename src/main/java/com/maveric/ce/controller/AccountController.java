package com.maveric.ce.controller;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.AccountResponseDto;
import com.maveric.ce.dto.AccountUpdateDto;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping("/create/{customerId}")
	public AccountResponseDto createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountDto accountDto) {

		return accountService.createAccount(customerId, accountDto);
	}

	@GetMapping("/{accountId}")
	public AccountResponseDto fetchAccountByNumber(@PathVariable(name = "accountId") Long accountNumber) {
		return accountService.fetchByAccountNumber(accountNumber);
	}

	@GetMapping("/customer/{customerId}")
	public List<AccountResponseDto> fetchAccountByCustomer(@PathVariable(name = "customerId") Long customerId) {
		return accountService.fetchAccountByCustomer(customerId);
	}

	@GetMapping("/fetchAll")
	public List<AccountResponseDto> fetchAllAccounts() {
		return accountService.fetchAllAccounts();
	}

	@PutMapping("/update/{customerId}")
	public AccountResponseDto updateAccount(@Valid @RequestBody AccountUpdateDto accountUpdateDto,
			@PathVariable Long customerId) {
		return accountService.updateAccount(accountUpdateDto, customerId);
	}

	@DeleteMapping("/delete/{accountNumber}/{customerId}")
	public String deleteAccount(@PathVariable(name = "accountNumber") Long accountNumber,
			@PathVariable(name = "customerId") Long customerId) {

		return accountService.deleteAccount(accountNumber, customerId);

	}
}
