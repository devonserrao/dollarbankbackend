package com.cognixia.jump.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.NegativeAmountException;
import com.cognixia.jump.exception.OverdraftException;
import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.Account;
import com.cognixia.jump.repository.AccountRepository;

@RequestMapping("/api")
@RestController
@CrossOrigin
public class AccountController {

	@Autowired
	AccountRepository repo;
	
	@GetMapping("/account")
	public List<Account> getAccounts() {
		return repo.findAll();
	}
	
	@PutMapping("/account/{id}/deposit/{amount}")
	public ResponseEntity<?> depositMoney(@PathVariable int id, @PathVariable double depositAmount) throws NegativeAmountException, ResourceNotFoundException {
		if(depositAmount < 0) {
			throw new NegativeAmountException("Please enter a non-negative amount!");
		}
		
		if(repo.existsById(id)) {
			repo.getById(id).setBalance(repo.getById(id).getBalance() + depositAmount);
			
			double updatedBalance = repo.getById(id).getBalance();
			return ResponseEntity.status(200).body(updatedBalance);
		}
		
		throw new ResourceNotFoundException("Couldn't find Account with id = " + id + "to deposit amount!");
	} 
	
	@PutMapping("/account/{id}/withdraw/{amount}")
	public ResponseEntity<?> withdrawMoney(@PathVariable int id, @PathVariable double withdrawAmount) throws NegativeAmountException, OverdraftException, ResourceNotFoundException {
		if(repo.existsById(id)) {
			if(repo.getById(id).getBalance() < withdrawAmount) {
				throw new OverdraftException("Cannot withdraw " + withdrawAmount + " as there isn't enough money in the account!");
			}
				
			repo.getById(id).setBalance(repo.getById(id).getBalance() - withdrawAmount);
			
			double updatedBalance = repo.getById(id).getBalance();
			return ResponseEntity.status(200).body(updatedBalance);
		}
		
		throw new ResourceNotFoundException("Couldn't find Account with id = " + id + "to deposit amount!");
	} 
	
	@PutMapping("/account/transfer")
	public ResponseEntity<?> transferMoneybetweenAccounts(@PathParam(value = "accountId1") int accountId1 ,
			@PathParam(value = "accountId2") int accountId2,
			@PathParam(value = "transferAmount") double transferAmount) throws ResourceNotFoundException, NegativeAmountException, OverdraftException {
		
		if(!repo.existsById(accountId1) || !repo.existsById(accountId2) )
			throw new ResourceNotFoundException("One of the accounts doesnt exist!");
		
		// if reach here - means both accounts exist
		
		if(transferAmount < 0) 
			throw new NegativeAmountException("Enter a non-negative transfer amount.");
		
		if(transferAmount > repo.getById(accountId1).getBalance())
			throw new OverdraftException("Not enough money in main account 1!");
		
		repo.getById(accountId1).setBalance(repo.getById(accountId1).getBalance() - transferAmount);
		repo.getById(accountId2).setBalance(repo.getById(accountId2).getBalance() + transferAmount);
	
		double updatedAccount1Balance = repo.getById(accountId1).getBalance();
		double updatedAccount2Balance = repo.getById(accountId2).getBalance();
		
		return ResponseEntity.status(200).body("Updated Account 1 Balance : " + updatedAccount1Balance + "\nUpdated Account 2 Balance : " + updatedAccount2Balance);
		
	}
}
