package com.tennisscoreboard.dao;

import com.tennisscoreboard.entity.Match;
import com.tennisscoreboard.entity.Player;
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
    public List<Match> getAllMatches() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Match m", Match.class).getResultList();
    }

    @Transactional(readOnly = true)
    public List<Match> getPlayerMatches(String playerName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "FROM Match m WHERE m.player1.name = :name OR m.player2.name = :name",
                        Match.class
                )
                .setParameter("name", playerName)
                .getResultList();
    }

    @Transactional
    public Match createMatch(Player player1, Player player2, Player winner) {
        Session session = sessionFactory.getCurrentSession();
        Match match = new Match(player1, player2, winner);
        session.persist(match);
        return match;
    }
}
