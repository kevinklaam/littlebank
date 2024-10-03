package com.example.littlebank.service;

import com.example.littlebank.model.Transaction;
import java.math.BigDecimal;

public interface BankingService {
    Transaction deposit(String username, BigDecimal amount);
    Transaction withdraw(String username, BigDecimal amount);
    Transaction transfer(String fromUsername, String toUsername, BigDecimal amount);
}
