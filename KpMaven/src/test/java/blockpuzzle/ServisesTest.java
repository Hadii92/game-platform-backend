package blockpuzzle;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.server.GameStudioServer;
import sk.tuke.gamestudio.service.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.List;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;


class ScoreServiceJDBCTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private ScoreServiceJDBC scoreService;
    @BeforeEach
    void setUp() throws SQLException {
        scoreService = new ScoreServiceJDBC();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM score");
        }
    }
    @Test
    void testAddScoreAndRetrieve() {
        Score score = new Score("Super Mario", "Grigory", 250, new java.util.Date());
        scoreService.addScore(score);
        List<Score> scores = scoreService.getTopScores("Super Mario");
        assertEquals(1, scores.size());
        assertEquals("Grigory", scores.get(0).getPlayer());
        assertEquals(250, scores.get(0).getPoints());
    }
    @Test
    void testReset() {
        scoreService.addScore(new Score("Super Mario", "Jan", 150, new java.util.Date()));
        scoreService.addScore(new Score("Super Mario", "Lopes", 300, new java.util.Date()));
        scoreService.reset();
        List<Score> scores = scoreService.getTopScores("Super Mario");
        assertTrue(scores.isEmpty());
    }
}
class RatingServiceJDBCTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private RatingServiceJDBC ratingService;
    @BeforeEach
    void setUp() throws SQLException {
        ratingService = new RatingServiceJDBC();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM rating");
        }
    }
    @Test
    void testSetAndGetRating() {
        Rating rating = new Rating("King Kong", "Ivan", 5, new Date());
        ratingService.setRating(rating);
        int retrievedRating = ratingService.getRating("King Kong", "Ivan");
        assertEquals(5, retrievedRating);
    }
    @Test
    void testAverageRating() {
        ratingService.setRating(new Rating("King Kong", "Lecya", 1, new Date()));
        ratingService.setRating(new Rating("King Kong", "Pontifik", 2, new Date()));
        int avg = ratingService.getAverageRating("King Kong");
        assertEquals(1, avg);
    }
    @Test
    void testReset() {
        ratingService.setRating(new Rating("King Kong", "Arnold", 3, new Date()));
        ratingService.reset();
        int avg = ratingService.getAverageRating("King Kong");
        assertEquals(0, avg);
    }
}
class CommentServiceJDBCTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private CommentServiceJDBC commentService;
    @BeforeEach
    void setUp() throws SQLException {
        commentService = new CommentServiceJDBC();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM comment");
        }
    }
    @Test
    void testAddAndRetrieveComment() {
        Comment comment = new Comment("Dota 2", "Amer", "GGWP!", new Date());
        commentService.addComment(comment);
        List<Comment> comments = commentService.getComments("Dota 2");
        assertEquals(1, comments.size());
        assertEquals("Amer", comments.get(0).getPlayer());
        assertEquals("GGWP!", comments.get(0).getComment());
    }
    @Test
    void testReset() {
        commentService.addComment(new Comment("Dota 2", "Sumail", "Pudge is missing!", new Date()));
        commentService.reset();
        List<Comment> comments = commentService.getComments("Dota 2");
        assertTrue(comments.isEmpty());
    }

}
class UserServiceJPATest {
    private static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private UserServiceJPA userServiceJPA;
    @Test
    @BeforeEach
    void setUp() throws SQLException {
        userServiceJPA = new UserServiceJPA();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM logged_user");
        }
    }
}
