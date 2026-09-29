package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Collections;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserController userController;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/profile")
    public String profile(Model model) {
        if (!userController.isLogged()) {
            return "redirect:/?error=You must be logged in to access the profile";
        }
        String username = userController.getLoggedUser().getLogin();
        int userRating = ratingService.getRating("BlockPuzzle", username);
        List<Score> scores = scoreService.getTopScores("BlockPuzzle");
        Score latestScore = scores.stream()
                .filter(score -> score.getPlayer().equals(username))
                .findFirst()
                .orElse(null);
        List<Comment> comments = commentService.getComments("BlockPuzzle");
        Collections.reverse(comments);
        Comment latestComment = comments.stream()
                .filter(comment -> comment.getPlayer().equals(username))
                .findFirst()
                .orElse(null);
        model.addAttribute("userRating", userRating);
        model.addAttribute("latestScore", latestScore);
        model.addAttribute("latestComment", latestComment);

        return "profile";
    }
}