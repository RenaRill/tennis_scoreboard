package com.tennisscoreboard.dao;

import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.entity.Player;
import com.tennisscoreboard.exception.DatabaseAccessException;
import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchDAO {
    private final SessionFactory sessionFactory;

    public MatchDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Match> getAllMatches(int offset, int limit) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery(
                            "FROM Match m " +
                                    "JOIN FETCH m.firstPlayer " +
                                    "JOIN FETCH m.secondPlayer " +
                                    "JOIN FETCH m.winner " +
                                    "ORDER BY m.id",
                            Match.class
                    )
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка загрузки матчей.", e);
        }
    }

    public List<Match> getPlayerMatches(String playerName, int offset, int limit) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery(
                            "FROM Match m " +
                                    "JOIN FETCH m.firstPlayer " +
                                    "JOIN FETCH m.secondPlayer " +
                                    "JOIN FETCH m.winner " +
                                    "WHERE m.firstPlayer.name = :name OR m.secondPlayer.name = :name " +
                                    "ORDER BY m.id",
                            Match.class
                    )
                    .setParameter("name", playerName)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка загрузки матчей игрока.", e);
        }
    }

    public Long countAllMatches() {
        try {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT COUNT(m) FROM Match m", Long.class)
                .getSingleResult();
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка подсчета матчей.", e);
        }
    }

    public Long countPlayerMatches(String playerName) {
        try {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT COUNT(m) FROM Match m " +
                        "WHERE m.firstPlayer.name = :name OR m.secondPlayer.name = :name",
                        Long.class)
                .setParameter("name", playerName)
                .getSingleResult();
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка подсчета матчей игрока.", e);
        }
    }

    public void saveMatch(Player player1, Player player2, Player winner) {
        try {
        Session session = sessionFactory.getCurrentSession();
        Match match = new Match(player1, player2, winner);
        session.persist(match);
        } catch (PersistenceException e) {
            throw new DatabaseAccessException("Ошибка сохранения матча.", e);
        }
    }
}
