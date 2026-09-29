
package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@NamedQuery( name = "Rating.getRatings",
        query = "SELECT r FROM Rating r WHERE r.game=:game ORDER BY r.rating DESC")
@NamedQuery( name = "Rating.resetRatings",
        query = "DELETE FROM Rating ")
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;
    private String game;
    private String player;
    private int rating;
    private Date ratedOn;

    public Rating(String game, String player, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }
    public Rating() {

    }
    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }
    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public Date getRatedOn() {
        return ratedOn;
    }
    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }
    @Override
    public String toString() {
        return "Rating{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", ratedOn=" + ratedOn +
                '}';
    }
}