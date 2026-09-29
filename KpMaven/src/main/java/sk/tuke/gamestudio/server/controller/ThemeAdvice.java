package sk.tuke.gamestudio.server.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class ThemeAdvice {
    @ModelAttribute("currentTheme")
    public String currentTheme(HttpSession session) {
        String theme = (String) session.getAttribute("theme");
        return theme != null ? theme : "dark";
    }
}