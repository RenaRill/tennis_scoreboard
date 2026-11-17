package com.tennisscoreboard;

import com.tennisscoreboard.exception.InvalidValueException;
import com.tennisscoreboard.model.OngoingMatch;
import org.junit.jupiter.api.*;

public class AddPointTest {
    private static final String FIRST_PLAYER = "Oliver";
    private static final String SECOND_PLAYER = "Jack";

    private OngoingMatch match;

    @BeforeEach
    void setupThis() {
        match = new OngoingMatch(FIRST_PLAYER, SECOND_PLAYER);
    }

    @Test
    void lovePoints_toFifteen() {
        match.addPoint(FIRST_PLAYER);
        Assertions.assertEquals("15", getPoints(FIRST_PLAYER));
        Assertions.assertEquals("0", getPoints(SECOND_PLAYER));
    }

    @Test
    void fortyPoints_winsGame() {
        simpleWinGame(FIRST_PLAYER);
        Assertions.assertEquals(1, getGames(FIRST_PLAYER));
        Assertions.assertEquals(0, getGames(SECOND_PLAYER));
    }

    @Test
    void opponentAdvantage_returnsToDeuce() {
        for (int i = 0; i < 4; i++) {
            match.addPoint(SECOND_PLAYER);
            match.addPoint(FIRST_PLAYER);
        }
        Assertions.assertEquals("40", getPoints(FIRST_PLAYER));
        Assertions.assertEquals("40", getPoints(SECOND_PLAYER));
    }

    @Test
    void advantagePoints_winsGame() {
        for (int i = 0; i < 3; i++) {
            match.addPoint(FIRST_PLAYER);
            match.addPoint(SECOND_PLAYER);
        }
        match.addPoint(FIRST_PLAYER);
        match.addPoint(FIRST_PLAYER);
        Assertions.assertEquals(1, getGames(FIRST_PLAYER));
        Assertions.assertEquals(0, getGames(SECOND_PLAYER));
    }

    @Test
    void sixGamesToZero_winsSet() {
        simpleWinSet();
        Assertions.assertEquals(1, getSets(FIRST_PLAYER));
        Assertions.assertEquals(0, getSets(SECOND_PLAYER));
    }

    @Test
    void sevenGamesToFive_winsSet() {
        for (int i = 0; i < 5; i++) {
            simpleWinGame(FIRST_PLAYER);
            simpleWinGame(SECOND_PLAYER);
        }
        simpleWinGame(FIRST_PLAYER);
        simpleWinGame(FIRST_PLAYER);
        Assertions.assertEquals(1, getSets(FIRST_PLAYER));
        Assertions.assertEquals(0, getSets(SECOND_PLAYER));
    }

    @Test
    void sixGamesToSix_startsTieBreak() {
        startTieBreak();
        Assertions.assertEquals("0", getPoints(FIRST_PLAYER));
        Assertions.assertEquals("0", getPoints(SECOND_PLAYER));
    }

    @Test
    void sevenTieBreakPointsToZero_winsSet() {
        startTieBreak();
        for (int i = 0; i < 7; i++) {
            match.addPoint(FIRST_PLAYER);
        }
        Assertions.assertEquals(1, getSets(FIRST_PLAYER));
        Assertions.assertEquals(0, getSets(SECOND_PLAYER));
    }

    @Test
    void nineTieBreakPointsToSeven_winsSet() {
        startTieBreak();
        for (int i = 0; i < 7; i++) {
            match.addPoint(FIRST_PLAYER);
            match.addPoint(SECOND_PLAYER);
        }
        match.addPoint(FIRST_PLAYER);
        match.addPoint(FIRST_PLAYER);
        Assertions.assertEquals(1, getSets(FIRST_PLAYER));
        Assertions.assertEquals(0, getSets(SECOND_PLAYER));
    }

    @Test
    void lovePointsAfterTieBreak_toFifteen() {
        startTieBreak();
        for (int i = 0; i < 7; i++) {
            match.addPoint(FIRST_PLAYER);
        }
        match.addPoint(FIRST_PLAYER);
        Assertions.assertEquals("15", getPoints(FIRST_PLAYER));
        Assertions.assertEquals("0", getPoints(SECOND_PLAYER));
    }

    @Test
    void twoSets_winsMatch() {
        simpleWinSet();
        simpleWinSet();
        Assertions.assertEquals(FIRST_PLAYER, match.getWinnerName().orElse(null));
    }

    @Test
    void unknownName_ThrowsException() {
        InvalidValueException exception = Assertions.assertThrows(
                InvalidValueException.class,
                () -> match.addPoint("Alice")
        );
        Assertions.assertEquals("Игрок с таким именем не найден.", exception.getMessage());
    }

    @Test
    void pointsAfterMatchWin_isIgnored() {
        simpleWinSet();
        simpleWinSet();
        match.addPoint(FIRST_PLAYER);
        Assertions.assertEquals("0", getPoints(FIRST_PLAYER));
        Assertions.assertEquals("0", getPoints(SECOND_PLAYER));
    }

    void simpleWinGame(String name) {
        match.addPoint(name);
        match.addPoint(name);
        match.addPoint(name);
        match.addPoint(name);
    }

    void simpleWinSet() {
        for (int i = 0; i < 6; i++) {
            simpleWinGame(FIRST_PLAYER);
        }
    }

    void startTieBreak() {
        for (int i = 0; i < 6; i++) {
            simpleWinGame(FIRST_PLAYER);
            simpleWinGame(SECOND_PLAYER);
        }
    }

    private String getPoints(String name) {
        return match.getPlayerScore(name).points();
    }

    private int getGames(String name) {
        return match.getPlayerScore(name).games();
    }

    private int getSets(String name) {
        return match.getPlayerScore(name).sets();
    }
}
