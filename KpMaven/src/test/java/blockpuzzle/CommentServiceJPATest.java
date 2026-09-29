package blockpuzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.server.GameStudioServer;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GameStudioServer.class)
@Transactional
public class CommentServiceJPATest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void clearComment() {
        entityManager.createQuery("DELETE FROM Comment ").executeUpdate();
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