package blockpuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.server.GameStudioServer;
import sk.tuke.gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public @SpringBootTest(classes = GameStudioServer.class)
@Transactional
class RatingServiceJPATest {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void clearRating() {
        entityManager.createQuery("DELETE FROM Rating").executeUpdate();
    }

    @Test
    public void testSetAndGetRating() {
        Rating rating = new Rating("King Kong", "Ivan", 5, new Date());
        ratingService.setRating(rating);
        int retrievedRating = ratingService.getRating("King Kong", "Ivan");
        assertEquals(5, retrievedRating);
    }

    @Test
    public void testAverageRating() {
        ratingService.setRating(new Rating("King Kong", "Lecya", 1, new Date()));
        ratingService.setRating(new Rating("King Kong", "Pontifik", 2, new Date()));
        int avg = ratingService.getAverageRating("King Kong");
        assertEquals(1, avg);
    }

    @Test
    public void testResetRating() {
        ratingService.setRating(new Rating("King Kong", "Arnold", 3, new Date()));
        ratingService.reset();
        int avg = ratingService.getAverageRating("King Kong");
        assertEquals(0, avg);
    }
}