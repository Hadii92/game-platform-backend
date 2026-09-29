package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.SessionAttributes;



import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.Block;
import sk.tuke.gamestudio.game.Field;
import sk.tuke.gamestudio.game.TileState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.ArrayDeque;
import java.util.Date;

@Controller
@RequestMapping("/blockgame")
@SessionAttributes({"field", "block", "difficulty", "moveHistory", "undoCount", "scoreSubmitted"})
public class BlockPuzzleController {
    @Autowired
    private UserController userController;
    @Autowired
    private final ScoreService scoreService;
    @Autowired
    private final RatingService ratingService;
    @Autowired
    private final CommentService commentService;

    public BlockPuzzleController(ScoreService scoreService, RatingService ratingService, CommentService commentService) {
        this.scoreService = scoreService;
        this.ratingService = ratingService;
        this.commentService = commentService;
    }

    public enum Difficulty {
        EASY(6), MEDIUM(9), HARD(12);

        private final int size;

        Difficulty(int size) {
            this.size = size;
        }
        public int getSize() {
            return size;
        }
    }

    private static class Move {
        private final Block block;
        private final int[] position;

        public Move(Block block, int row, int col) {
            this.block = new Block(block.getShape(), block.getColor());
            this.position = new int[]{row, col};
        }

        public Block getBlock() {
            return block;
        }

        public int[] getPosition() {
            return position;
        }
    }


    @ModelAttribute("block")
    public Block getBlock() {
        return Block.getRandomBlock();
    }

    @ModelAttribute("difficulty")
    public Difficulty getDifficulty() {
        return Difficulty.EASY;
    }

    @ModelAttribute("moveHistory")
    public ArrayDeque<Move> getMoveHistory() {
        return new ArrayDeque<>(3);
    }

    @ModelAttribute("undoCount")
    public Integer getUndoCount() {
        return 0;
    }

    @ModelAttribute("scoreSubmitted")
    public Boolean getScoreSubmitted() {
        return false;
    }

    @GetMapping
    public String game(Model model, @ModelAttribute("field") Field field, @ModelAttribute("block") Block block,
                       @ModelAttribute("difficulty") Difficulty difficulty, @ModelAttribute("moveHistory") ArrayDeque<Move> moveHistory,
                       @ModelAttribute("undoCount") Integer undoCount, @ModelAttribute("scoreSubmitted") Boolean scoreSubmitted) {
        boolean gameOver = !field.hasSpaceForBlock(block);
        if (gameOver && !scoreSubmitted) {
            String username = userController.isLogged() ? userController.getLoggedUser().getLogin() : "guest";
            scoreService.addScore(new Score("BlockPuzzle", username, field.getScore(), new Date()));
            model.addAttribute("scoreSubmitted", true);
        }
        model.addAttribute("score", field.getScore());
        model.addAttribute("comments", commentService.getComments("BlockPuzzle"));
        model.addAttribute("rating", ratingService.getAverageRating("BlockPuzzle"));
        model.addAttribute("tiles", field.getTiles());
        model.addAttribute("currentBlock", block.getShape());
        model.addAttribute("currentBlockColor", block.getColor());
        model.addAttribute("scores", scoreService.getTopScores("BlockPuzzle"));
        model.addAttribute("gameOver", gameOver);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("canUndo", !moveHistory.isEmpty() && undoCount < 3);
        return "game";
    }

    @PostMapping("/place")
    public String placeBlock(@RequestParam int row, @RequestParam int col,
                             @ModelAttribute("field") Field field, @ModelAttribute("block") Block block,
                             @ModelAttribute("moveHistory") ArrayDeque<Move> moveHistory, Model model) {
        if (field.canPlaceBlock(row - 1, col - 1, block)) {
            if (moveHistory.size() >= 3) {
                moveHistory.removeFirst();
            }
            moveHistory.addLast(new Move(block, row - 1, col - 1));
            field.placeBlock(row - 1, col - 1, block);
            Block newBlock = Block.getRandomBlock();
            model.addAttribute("block", newBlock);
        }
        return "redirect:/blockgame";
    }

    @PostMapping("/undo")
    public String undoMove(@ModelAttribute("field") Field field, @ModelAttribute("moveHistory") ArrayDeque<Move> moveHistory,
                           @ModelAttribute("undoCount") Integer undoCount, Model model) {
        if (!moveHistory.isEmpty() && undoCount < 3) {
            Move lastMove = moveHistory.removeLast();
            Block lastBlock = lastMove.getBlock();
            int[] position = lastMove.getPosition();
            int[][] shape = lastBlock.getShape();
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        int r = position[0] + i;
                        int c = position[1] + j;
                        field.getTiles()[r][c].setState(TileState.FREE);
                        field.getTiles()[r][c].setColor(null);
                    }
                }
            }
            model.addAttribute("block", lastBlock);
            model.addAttribute("undoCount", undoCount + 1);
        }
        return "redirect:/blockgame";
    }

    @PostMapping("/setDifficulty")
    public String setDifficulty(@RequestParam Difficulty difficulty, Model model) {
        System.out.println("Selected difficulty: " + difficulty);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("field", new Field(difficulty.getSize(), difficulty.getSize()));
        model.addAttribute("block", Block.getRandomBlock());
        model.addAttribute("scoreSubmitted", false);
        return "redirect:/blockgame";
    }

    @ModelAttribute("field")
    public Field getField(@ModelAttribute("difficulty") Difficulty difficulty, Model model) {
        System.out.println("Difficulty in getField: " + difficulty);
        return model.containsAttribute("field") ? (Field) model.getAttribute("field") : new Field(difficulty.getSize(), difficulty.getSize());
    }

    @PostMapping("/newgame")
    public String newGame(SessionStatus status, Model model, @ModelAttribute("field") Field field) {
        String username = userController.isLogged() ? userController.getLoggedUser().getLogin() : "guest";
        scoreService.addScore(new Score("BlockPuzzle", username, field.getScore(), new Date()));
        status.setComplete();
        return "redirect:/blockgame";
    }

    @PostMapping("/rate")
    public String submitRating(@RequestParam int rating, @ModelAttribute("field") Field field, @ModelAttribute("block") Block block) {
        if (!field.hasSpaceForBlock(block)) {
            if (rating >= 1 && rating <= 5) {
                String username = userController.isLogged() ? userController.getLoggedUser().getLogin() : "guest";
                ratingService.setRating(new Rating("BlockPuzzle", username, rating, new Date()));
            }
        }
        return "redirect:/blockgame";
    }

    @PostMapping("/comment")
    public String submitComment(@RequestParam String comment, @ModelAttribute("field") Field field, @ModelAttribute("block") Block block) {
        if (!field.hasSpaceForBlock(block)) {
            if (comment != null) {
                String username = userController.isLogged() ? userController.getLoggedUser().getLogin() : "guest";
                commentService.addComment(new sk.tuke.gamestudio.entity.Comment("BlockPuzzle", username, comment, new Date()));
            }
        }
        return "redirect:/blockgame";
    }
}