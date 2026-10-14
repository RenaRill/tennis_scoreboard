package com.tennisscoreboard.dao;

import com.tennisscoreboard.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlayerDAO {
    private final SessionFactory sessionFactory;

    public PlayerDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    public Player getPlayerById(Long playerId) {
//        Session session = sessionFactory.getCurrentSession();
//        return session.find(Player.class, playerId);
//    }

    @Transactional(readOnly = true)
    public Player getPlayerByName(String playerName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", playerName)
                .getSingleResult();
    }

    @Transactional
    public Player createPlayer(String playerName) {
        Session session = sessionFactory.getCurrentSession();
        Player player = new Player(playerName);
        session.persist(player);
        return player;
    }
}
