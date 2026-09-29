package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModalController {

    @GetMapping("/modal")
    public String showModal() {
        return "modal";
    }
}