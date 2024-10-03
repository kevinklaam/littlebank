package com.example.littlebank.service;

import com.example.littlebank.model.Transaction;
import com.example.littlebank.model.User;
import com.example.littlebank.repository.UserRepository;
import com.example.littlebank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BankingServiceImpl implements BankingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("0.00");

    @Override
    @Transactional
    public Transaction deposit(String username, BigDecimal amount) throws IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        Transaction transaction = new Transaction(user, "DEPOSIT", amount, LocalDateTime.now(), "Deposit to account");
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction withdraw(String username, BigDecimal amount) throws IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        BigDecimal newBalance = user.getBalance().subtract(amount);
        if (newBalance.compareTo(MINIMUM_BALANCE) < 0) {
            throw new IllegalArgumentException("Withdrawal would result in balance below minimum requirement");
        }

        user.setBalance(newBalance);
        userRepository.save(user);

        Transaction transaction = new Transaction(user, "WITHDRAWAL", amount.negate(), LocalDateTime.now(), "Withdrawal from account");
        return transactionRepository.save(transaction);
    }
          @Override
          @Transactional
          public Transaction transfer(String fromUsername, String toUsername, BigDecimal amount) throws IllegalArgumentException {
              if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                  throw new IllegalArgumentException("Transfer amount must be positive");
              }

              User fromUser = userRepository.findByUsername(fromUsername)
                      .orElseThrow(() -> new IllegalArgumentException("From user not found"));
              User toUser = userRepository.findByUsername(toUsername)
                      .orElseThrow(() -> new IllegalArgumentException("To user not found"));

              BigDecimal newBalance = fromUser.getBalance().subtract(amount);
              if (newBalance.compareTo(MINIMUM_BALANCE) < 0) {
                  throw new IllegalArgumentException("Transfer would result in balance below minimum requirement");
              }

              fromUser.setBalance(newBalance);
              toUser.setBalance(toUser.getBalance().add(amount));
              userRepository.save(fromUser);
              userRepository.save(toUser);

              // Create and save transaction for sender
              Transaction senderTransaction = new Transaction(fromUser, "TRANSFER", amount.negate(), LocalDateTime.now(), "Transfer to " + toUsername);
              transactionRepository.save(senderTransaction);

              // Create and save transaction for recipient
              Transaction recipientTransaction = new Transaction(toUser, "TRANSFER", amount, LocalDateTime.now(), "Transfer from " + fromUsername);
              transactionRepository.save(recipientTransaction);

              return senderTransaction;
          }
    
}
