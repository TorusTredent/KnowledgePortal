package by.knowledgeportal.controller;

import by.knowledgeportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/update/{userId}")
    public String usersUpdate(@PathVariable Long userId) {
        return "redirect:/admin/users";
    }
    @PostMapping("/users/delete/{userId}")
    public String usersDelete(@PathVariable Long userId) {
        return "redirect:/admin/users";
    }
}
