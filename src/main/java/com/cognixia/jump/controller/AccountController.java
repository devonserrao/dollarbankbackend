package com.cognixia.jump.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.Account;
import com.cognixia.jump.repository.AccountRepository;

@RequestMapping("/api")
@RestController
public class AccountController {

	@Autowired
	AccountRepository repo;
	
	@GetMapping("/account")
	public List<Account> getAccounts() {
		return repo.findAll();
	}
	
	@PutMapping("/account/{id}/deposit/{amount}")
	public ResponseEntity<?> depositMoney(@PathVariable int id, @PathVariable double depositAmount) {
		if(repo.existsById(id)) {
			repo.getById(id).setBalance(repo.getById(id).getBalance() + depositAmount);
			
			double updatedBalance = repo.getById(id).getBalance();
			return ResponseEntity.status(200).body(updatedBalance);
		}
		
		return ResponseEntity.status(400).body("Couldn't find Account with id = " + id + "to deposit amount!");
	} 
	
	@PutMapping("/account/{id}/withdraw/{amount}")
	public ResponseEntity<?> withdrawMoney(@PathVariable int id, @PathVariable double withdrawAmount) {
		if(repo.existsById(id)) {
			if(repo.getById(id).getBalance() < withdrawAmount) {
				return ResponseEntity.status(400).body("Cannot withdraw " + withdrawAmount + " as there isn't enough money in the account!");
			}
				
			repo.getById(id).setBalance(repo.getById(id).getBalance() - withdrawAmount);
			
			double updatedBalance = repo.getById(id).getBalance();
			return ResponseEntity.status(200).body(updatedBalance);
		}
		
		return ResponseEntity.status(400).body("Couldn't find Account with id = " + id + "to deposit amount!");
	} 
	// Withdraw
	// Deposit
	// Transfer between accounts
}
