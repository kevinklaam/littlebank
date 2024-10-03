package com.example.littlebank.controller;

import com.example.littlebank.model.User;
import com.example.littlebank.model.Transaction;
import com.example.littlebank.repository.UserRepository;
import com.example.littlebank.repository.TransactionRepository;
import com.example.littlebank.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/api/banking")
public class BankingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankingService bankingService;
      @GetMapping("/account")
      public String getAccount(Principal principal, Model model) {
          User user = userRepository.findByUsername(principal.getName())
                  .orElseThrow(() -> new RuntimeException("User not found"));
          List<Transaction> transactions = transactionRepository.findByUserOrderByDateDesc(user);
          model.addAttribute("user", user);
          model.addAttribute("transactions", transactions);
          return "account";
      }

    @PostMapping("/deposit")
    public String deposit(Principal principal, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes) {
        try {
            Transaction transaction = bankingService.deposit(principal.getName(), amount);
            redirectAttributes.addFlashAttribute("message", "Deposit successful. Transaction ID: " + transaction.getId());
            redirectAttributes.addFlashAttribute("success", true);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/api/banking/account";
    }

    @PostMapping("/withdraw")
    public String withdraw(Principal principal, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes) {
        try {
            Transaction transaction = bankingService.withdraw(principal.getName(), amount);
            redirectAttributes.addFlashAttribute("message", "Withdrawal successful. Transaction ID: " + transaction.getId());
            redirectAttributes.addFlashAttribute("success", true);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/api/banking/account";
    }

    @PostMapping("/transfer")
    public String transfer(Principal principal, @RequestParam String toUsername, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes) {
        try {
            Transaction transaction = bankingService.transfer(principal.getName(), toUsername, amount);
            redirectAttributes.addFlashAttribute("message", "Transfer successful. Transaction ID: " + transaction.getId());
            redirectAttributes.addFlashAttribute("success", true);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/api/banking/account";
    }    
}