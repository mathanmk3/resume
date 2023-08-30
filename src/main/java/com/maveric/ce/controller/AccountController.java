package com.maveric.ce.controller;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.AccountResponseDto;
import com.maveric.ce.dto.AccountUpdateDto;
import com.maveric.ce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;


	@PostMapping("/create/{customerId}")
	public AccountResponseDto createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountDto accountDto) {

		return accountService.createAccount(customerId, accountDto);
	}

	@GetMapping("/fetch/{customerId}/{accountNumber}")
	public AccountResponseDto fetchAccountByNumber(@PathVariable(name="customerId") Long customerId ,@PathVariable(name = "accountNumber") Long accountNumber) {
		return accountService.fetchByAccountNumber(customerId,accountNumber);
	}

	@GetMapping("/fetchAll/{customerId}")
	public List<AccountResponseDto> fetchAccountByCustomer(@PathVariable(name = "customerId") Long customerId) {
		return accountService.fetchAccountByCustomer(customerId);
	}

	// this is not used since we cant list all accounts without customer input.
	@GetMapping("/fetchAll")
	public List<AccountResponseDto> fetchAllAccounts() {
		return accountService.fetchAllAccounts();
	}

	@PutMapping("/update/{customerId}/{accountNumber}")
	public AccountResponseDto updateAccount(@Valid @RequestBody AccountUpdateDto accountUpdateDto,@PathVariable Long customerId,
			@PathVariable Long accountNumber) {
		return accountService.updateAccount(accountUpdateDto, customerId, accountNumber);
	}

	@DeleteMapping("/delete/{customerId}/{accountNumber}")
	public String deleteAccount(@PathVariable(name = "accountNumber") Long accountNumber,
			@PathVariable(name = "customerId") Long customerId) {

		return accountService.deleteAccount(accountNumber, customerId);

	}
}
