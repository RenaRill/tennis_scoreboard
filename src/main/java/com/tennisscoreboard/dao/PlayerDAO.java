package com.tennisscoreboard.dao;

import com.tennisscoreboard.entity.Player;
import com.tennisscoreboard.exception.DatabaseAccessException;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerDAO {
    private final SessionFactory sessionFactory;

    public PlayerDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Player> getPlayerByName(String playerName) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Player p WHERE p.name = :name", Player.class)
                    .setParameter("name", playerName)
                    .uniqueResultOptional();
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка загрузки игрока по имени.", e);
        }
    }

    public Player savePlayer(String playerName) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Player player = new Player(playerName);
            session.persist(player);
            return player;
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка сохранения игрока.", e);
        }
    }
}
