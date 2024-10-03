package com.example.littlebank.repository;

import com.example.littlebank.model.Transaction;
import com.example.littlebank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByDateDesc(User user);
}
