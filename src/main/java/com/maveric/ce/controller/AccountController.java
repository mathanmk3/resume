package com.maveric.ce.controller;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.AccountResponseDto;
import com.maveric.ce.dto.AccountUpdateDto;
import com.maveric.ce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;


	@PostMapping("/create/{customerId}")
	public ResponseEntity<AccountResponseDto> createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountDto accountDto) {

		return new ResponseEntity<>(accountService.createAccount(customerId, accountDto), HttpStatus.OK);
	}

	@GetMapping("/fetch/{customerId}/{accountNumber}")
	public  ResponseEntity<AccountResponseDto>  fetchAccountByNumber(@PathVariable(name="customerId") Long customerId ,@PathVariable(name = "accountNumber") Long accountNumber) {
		return  new ResponseEntity<>(accountService.fetchByAccountNumber(customerId,accountNumber),HttpStatus.OK);
	}

	@GetMapping("/fetchAll/{customerId}")
	public ResponseEntity<List<AccountResponseDto>> fetchAccountByCustomer(@PathVariable(name = "customerId") Long customerId) {
		return  new ResponseEntity<>(accountService.fetchAccountByCustomer(customerId),HttpStatus.OK);
	}

	@GetMapping("/getAll/{customerId}")
	public ResponseEntity<List<AccountResponseDto>> getAccountByCustomer(@PathVariable(name = "customerId") Long customerId) {
		return  new ResponseEntity<>(accountService.fetchAccountByCustomer(customerId),HttpStatus.OK);
	}

	// this is not used since we cant list all accounts without customer input.
	/*@GetMapping("/fetchAll")
	public ResponseEntity<List<AccountResponseDto>>fetchAllAccounts() {
		return  new ResponseEntity<>(accountService.fetchAllAccounts(),HttpStatus.OK);
	}*/

	@PutMapping("/update/{customerId}/{accountNumber}")
	public ResponseEntity<AccountResponseDto> updateAccount(@Valid @RequestBody AccountUpdateDto accountUpdateDto,@PathVariable Long customerId,
			@PathVariable Long accountNumber) {
		return  new ResponseEntity<>(accountService.updateAccount(accountUpdateDto, customerId, accountNumber),HttpStatus.OK);
	}

	@DeleteMapping("/delete/{customerId}/{accountNumber}")
	public ResponseEntity<Boolean> deleteAccount(@PathVariable(name = "accountNumber") Long accountNumber,
			@PathVariable(name = "customerId") Long customerId) {

		return new ResponseEntity<>(accountService.deleteAccount(accountNumber, customerId),HttpStatus.OK);

	}
}
