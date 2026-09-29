package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.tuke.gamestudio.service.ScoreService;
@Controller
@RequestMapping
public class HallOfFameController {
    @Autowired
    private final ScoreService scoreService;

    public HallOfFameController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/hall-of-fame")
    public String hallOfFame(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("BlockPuzzle"));
        return "hall_of_fame";
    }
}
