package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserServiceJPA;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserController {
    private User loggedUser;

    @Autowired
    private UserServiceJPA userService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/register")
    public String register(String login, String password) {
        try {
            User newUser = new User(login, password);
            userService.addUser(newUser);
            return "redirect:/?message=Registration successful";
        } catch (IllegalArgumentException e) {
            return "redirect:/?error=Login already exists";
        }
    }

    @RequestMapping("/login")
    public String login(String login, String password) {
        User user = userService.findUserByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            loggedUser = user;
            return "redirect:/blockgame";
        }
        return "redirect:/?error=Invalid login or password";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    @PostMapping("/toggle-theme")
    public ResponseEntity<String> toggleTheme(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String current = (String) session.getAttribute("theme");
        String newTheme = "dark".equals(current) ? "light" : "dark";
        session.setAttribute("theme", newTheme);
        return ResponseEntity.ok(newTheme);
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}