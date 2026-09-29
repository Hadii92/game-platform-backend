package sk.tuke.gamestudio.game;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;

public class ConsoleUI {
    private Scanner scanner = new Scanner(System.in);
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;

    public void play() {
        System.out.println("Enter your name:");
        scanner.nextLine();
        String playerName = scanner.nextLine();
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy (6x6)");
        System.out.println("2. Medium (9x9)");
        System.out.println("3. Hard (12x12)");

        int difficulty = 0;
        while (difficulty < 1 || difficulty > 3) {
            try {
                difficulty = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.next(); // Пропускаем неверный ввод
            }
        }

        Field field = new Field(difficulty == 1 ? 6 : (difficulty == 2 ? 9 : 12), difficulty == 1 ? 6 : (difficulty == 2 ? 9 : 12));

        while (true) {
            field.display();
            Block block = Block.getRandomBlock();
            if (!field.hasSpaceForBlock(block)) {
                System.out.println("Game over! No more space for blocks.");
                System.out.println("Last block:");
                displayBlockShape(block);
                int finalScore = field.getScore();
                System.out.println("Your final score: " + finalScore);
                scoreService.addScore(new Score("BlockPuzzle", playerName, finalScore, new Date()));
                displayTopScores();
                feedbackRating(playerName);
                feedbackComment(playerName);
                break;
            }
            System.out.println("Your block:");
            displayBlockShape(block);
            System.out.println("Enter row and column to place the block (or type 'exit' to quit):");

            String input = scanner.next();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("You have exited the game.");
                int finalScore = field.getScore();
                scoreService.addScore(new Score("BlockPuzzle", playerName, finalScore, new Date()));
                feedbackRating(playerName);
                feedbackComment(playerName);
                break;
            }

            try {
                int row = Integer.parseInt(input) - 1;
                int col = scanner.nextInt() - 1;

                if (field.canPlaceBlock(row, col, block)) {
                    field.placeBlock(row, col, block);
                } else {
                    System.out.println("Cannot place block here! Try again.");
                    displayBlockShape(block);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter valid numbers.");
                scanner.next();
            }
        }
    }

    private void displayBlockShape(Block block) {
        int[][] shape = block.getShape();
        String color = block.getColor();
        for (int[] row : shape) {
            for (int cell : row) {
                if (cell == 1) {
                    System.out.print(color + "■ " + "\u001B[0m"); // Цветной блок
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private void displayTopScores() {
        System.out.println("\n=== Top Scores ===");
        List<Score> scores = scoreService.getTopScores("BlockPuzzle");
        for (Score score : scores) {
            System.out.println(score.getPlayer() + ": " + score.getPoints() + " points");
        }
    }

    private void feedbackRating(String playerName) {
        String response;
        do {
            System.out.println("Would you like to rate the game? (yes/no)");
            response = scanner.next().trim().toLowerCase();
        } while (!response.equals("yes") && !response.equals("no"));

        if (response.equals("yes")) {
            int ratingValue;
            do {
                System.out.println("Enter your rating (1-5):");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5:");
                    scanner.next(); // Пропускаем неверный ввод
                }
                ratingValue = scanner.nextInt();
            } while (ratingValue < 1 || ratingValue > 5);

            Rating rating = new Rating("BlockPuzzle", playerName, ratingValue, new java.util.Date());
            ratingService.setRating(rating);
            System.out.println("Thank you for your rating!");
        } else {
            System.out.println("Thank you for playing!");
        }
    }

    private void feedbackComment(String playerName) {
        String response;
        do {
            System.out.println("Would you like to leave a comment? (yes/no)");
            response = scanner.next().trim().toLowerCase();
        } while (!response.equals("yes") && !response.equals("no"));

        if (response.equals("yes")) {
            System.out.println("Enter your comment:");
            scanner.nextLine();
            String commentText = scanner.nextLine();
            Comment comment = new Comment("BlockPuzzle", playerName, commentText, new java.util.Date());
            commentService.addComment(comment);
            System.out.println("Thank you for your comment!");
        }
    }
    public void showMainMenu() {
        while (true) {
            System.out.println("==== BLOCK PUZZLE ====");
            System.out.println("1. New Game");
            System.out.println("2. Top Scores");
            System.out.println("3. Exit");
            String choice = scanner.next();
            switch (choice) {
                case "1":
                    play();
                    break;
                case "2":
                    displayTopScores();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
