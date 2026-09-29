package blockpuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.server.GameStudioServer;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GameStudioServer.class)
@Transactional
public class ScoreServiceJPATest {

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void clearScore() {
        entityManager.createQuery("DELETE FROM Score ").executeUpdate();
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