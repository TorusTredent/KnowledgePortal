package by.knowledgeportal.controller;

import by.knowledgeportal.entity.Record;
import by.knowledgeportal.entity.User;
import by.knowledgeportal.service.RecordService;
import by.knowledgeportal.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RecordService recordService;

    @GetMapping("/auth")
    public String auth(Model model) {
        return "user/auth";
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        model.addAttribute("regUser", new User());
        return "user/reg";
    }

    @PostMapping("/reg")
    public String reg(@ModelAttribute("regUser") User user, Model model) {
        User userDb = userService.findByEmail(user.getEmail());

        if (userDb != null) {
            model.addAttribute("message", "Email is already exist");
            model.addAttribute("regUser", user);
            return "user/reg";
        }

        userService.save(user);

        return "redirect:/user/auth";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Record> records = recordService.getAllRecordsByAuthor(user);
        model.addAttribute("profile", user);
        model.addAttribute("records", records);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("profile") User user, Model model) {
        userService.update(user, userDetails);
        return "redirect:/user/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            // удаление куков
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            // удаление токена
            response.setHeader("Authorization", "");
        }
        return "redirect:/user/auth";
    }
}
