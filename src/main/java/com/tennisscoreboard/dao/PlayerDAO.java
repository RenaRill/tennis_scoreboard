package com.tennisscoreboard.dao;

import com.tennisscoreboard.entity.Player;
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
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", playerName)
                .uniqueResultOptional();
    }

    public Player savePlayer(String playerName) {
        Session session = sessionFactory.getCurrentSession();
        Player player = new Player(playerName);
        session.persist(player);
        return player;
    }
}
