package sk.tuke.gamestudio.service;

import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceJPA {
    @PersistenceContext
    private EntityManager entityManager;

    public void addUser(User user) {
        if (isLoginTaken(user.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        entityManager.persist(user);
    }
    public User findUserByLogin(String login) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean isLoginTaken(String login) {
        return findUserByLogin(login) != null;
    }
}