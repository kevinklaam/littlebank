package com.example.littlebank.controller;

import com.example.littlebank.model.User;
import com.example.littlebank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
      @PostMapping("/register")
      public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
          try {
              userService.registerUser(user.getUsername(), user.getPassword());
              redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
              redirectAttributes.addFlashAttribute("success", true);
              return "redirect:/login";
          } catch (RuntimeException e) {
              redirectAttributes.addFlashAttribute("message", "Registration failed: " + e.getMessage());
              redirectAttributes.addFlashAttribute("success", false);
              return "redirect:/register";
          }
      }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("message", "Invalid username or password");
        model.addAttribute("success", false);
        return "login";
    }
}