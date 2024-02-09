package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

//   ## 1: Our API should be able to process new User registrations.

//   As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register.
//  The body will contain a representation of a JSON Account, but will not contain an account_id.

@PostMapping("register")
public ResponseEntity<Account> createNewAccount(@RequestBody Account newAccount ){
    return this.accountService.createAccount(newAccount);
}

// ## 2: Our API should be able to process User logins.

// As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
// The request body will contain a JSON representation of an Account.

@PostMapping("login")
public ResponseEntity<Account> login(@RequestBody Account account){
    return this.accountService.login(account);
}

}
