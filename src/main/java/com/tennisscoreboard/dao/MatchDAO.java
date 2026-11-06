package com.tennisscoreboard.dao;

import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.entity.Player;
import com.tennisscoreboard.util.Pagination;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MatchDAO {
    private final SessionFactory sessionFactory;

    public MatchDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatches(int page) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Match m " +
                                "JOIN FETCH m.player1 " +
                                "JOIN FETCH m.player2 " +
                                "JOIN FETCH m.winner " +
                                "ORDER BY m.id",
                        Match.class
                )
                .setFirstResult((page - 1) * Pagination.PAGE_SIZE)
                .setMaxResults(Pagination.PAGE_SIZE)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Match> getPlayerMatches(int page, String playerName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Match m " +
                                "JOIN FETCH m.player1 " +
                                "JOIN FETCH m.player2 " +
                                "JOIN FETCH m.winner " +
                                "WHERE m.player1.name = :name OR m.player2.name = :name " +
                                "ORDER BY m.id",
                        Match.class
                )
                .setParameter("name", playerName)
                .setFirstResult((page - 1) * Pagination.PAGE_SIZE)
                .setMaxResults(Pagination.PAGE_SIZE)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Long countAllMatches() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT COUNT(m) FROM Match m", Long.class)
                .getSingleResult();
    }

    @Transactional(readOnly = true)
    public Long countPlayerMatches(String playerName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT COUNT(m) FROM Match m WHERE m.player1.name = :name OR m.player2.name = :name",
                        Long.class)
                .setParameter("name", playerName)
                .getSingleResult();
    }

    @Transactional
    public void saveMatch(Player player1, Player player2, Player winner) {
        Session session = sessionFactory.getCurrentSession();
        Match match = new Match(player1, player2, winner);
        session.persist(match);
    }
}
