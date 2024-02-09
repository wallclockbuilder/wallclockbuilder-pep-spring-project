package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;



public interface AccountRepository extends JpaRepository<Account, Long>{
    List<Account> findByUsername(String username);
}
