package org.example.goajaspring.controller;

import org.example.goajaspring.model.User;
import org.example.goajaspring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user, RedirectAttributes ra) {
        user.setRole("USER");
        userService.saveUser(user);
        ra.addFlashAttribute("success", "Akun berhasil dibuat. Silakan login.");
        return "redirect:/login";
    }
}