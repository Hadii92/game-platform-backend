
package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void setRating(Rating rating) throws RatingException {
        Rating existing = entityManager.createQuery(
                        "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player", Rating.class)
                .setParameter("game", rating.getGame())
                .setParameter("player", rating.getPlayer())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setRating(rating.getRating());
            existing.setRatedOn(rating.getRatedOn());
        } else {
            entityManager.persist(rating);
        }
    }
    @Override
    public int getAverageRating(String game) throws RatingException {
        Double avgRating = (Double) entityManager.createQuery("SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
                .setParameter("game", game).getSingleResult();
        if (avgRating != null){
            return avgRating.intValue();
        }else {
            return 0;
        }
    }
    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Integer rating = (Integer) entityManager.createQuery(
                            "SELECT r.rating FROM Rating r WHERE r.game = :game AND r.player = :player")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return rating != null ? rating : 0;
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            throw new RatingException("Error getting rating", e);
        }
    }
    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}
