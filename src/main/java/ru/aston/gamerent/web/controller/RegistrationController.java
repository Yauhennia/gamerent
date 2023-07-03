package ru.aston.gamerent.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.aston.gamerent.model.dto.RegistrationUser;
import ru.aston.gamerent.service.EmailService;
import ru.aston.gamerent.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private final EmailService emailService;

    private static final String INDEX_PAGE = "index";

    private static final String REGISTRATION_PAGE = "registration";

    @GetMapping("/registration")
    public String newUser(@ModelAttribute("user") RegistrationUser user) {
        return REGISTRATION_PAGE;
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid RegistrationUser user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTRATION_PAGE;
        }
        if (!user.password().equals(user.passwordConfirm())) {
            model.addAttribute("passwordError", "Passwords does not match!");
            return REGISTRATION_PAGE;
        }
        if (!userService.saveUser(user)) {
            model.addAttribute("emailError", String.format("User with email %s already exists!", user.email()));
            return REGISTRATION_PAGE;
        }
        String message = emailService.sendRegistrationMail(user.username(), user.email(), user.password());
        model.addAttribute("registrationMessage", message);
        return INDEX_PAGE;
    }
}