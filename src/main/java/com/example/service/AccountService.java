package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

// - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long,
//  and an Account with that username does not already exist. If all these conditions are met, the response body should 
// contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. 
// The new account should be persisted to the database.
// - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
// - If the registration is not successful for some other reason, the response status should be 400. (Client error

    public ResponseEntity<Account> createAccount(Account account) {
        List<Account> existingAccounts = this.accountRepository.findByUsername(account.getUsername());
        String username = account.getUsername();
        int passwordLength = account.getPassword().length();
        if(!existingAccounts.isEmpty()){
            return ResponseEntity.status(409).body(existingAccounts.get(0));
        }else if(username != "" && passwordLength > 4 && existingAccounts.isEmpty()){
            Account savedAccount = this.accountRepository.save(account);
            return ResponseEntity.status(200).body(savedAccount);
        }
        return ResponseEntity.status(400).body(account);
    }

// - The login will be successful if and only if the username and password provided in the 
// request body JSON match a real account existing on the database. If successful, the 
// response body should contain a JSON of the account in the response body, including 
// its account_id. The response status should be 200 OK, which is the default.
// - If the login is not successful, the response status should be 401. (Unauthorized)
    public ResponseEntity<Account> login(Account account){
        String username = account.getUsername();
        List<Account> foundAccounts = this.accountRepository.findByUsername(username);

        if(!foundAccounts.isEmpty()){
            Account foundAccount = foundAccounts.get(0);
            String accountUsername = account.getUsername();
            String foundUsername = foundAccount.getUsername();
            String foundPassword = foundAccount.getPassword();
            String accountPassword = account.getPassword();
            if(foundUsername.equals(accountUsername) && foundPassword.equals(accountPassword)){
                return ResponseEntity.status(200).body(foundAccount);
            }
        }
        
        return ResponseEntity.status(401).body(account);
    }
}
