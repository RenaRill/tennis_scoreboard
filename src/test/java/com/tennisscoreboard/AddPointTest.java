package com.tennisscoreboard;

import com.tennisscoreboard.dao.MatchDAO;
import com.tennisscoreboard.dao.PlayerDAO;
import com.tennisscoreboard.exception.InvalidValueException;
import com.tennisscoreboard.infrastructure.OngoingMatchStorage;
import com.tennisscoreboard.model.OngoingMatch;
import com.tennisscoreboard.service.MatchService;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.mockito.Mockito.mock;

public class AddPointTest {
    private PlayerDAO playerDAO;
    private MatchDAO matchDAO;
    private OngoingMatchStorage matchStorage;

    private MatchService matchService;
    private OngoingMatch match;
    private UUID id;

    @BeforeEach
    void setupThis(){
        playerDAO = mock(PlayerDAO.class);
        matchDAO = mock(MatchDAO.class);
        matchStorage = new OngoingMatchStorage();
        matchService = new MatchService(playerDAO, matchDAO, matchStorage);

        match = new OngoingMatch("Oliver", "Jack");
        id = matchService.startMatch(match);
    }

    // Обычное увеличение очка: 0-0 -> 15-0
    @Test
    void testAddPoint()
    {
        matchService.addPoint(id, "Oliver"); // 15-0
        Assertions.assertEquals("15", match.getPlayerScore("Oliver").getPointsScore());
    }

    // Обычный выигрыш гейма: 40-0 -> win game
    @Test
    void testAddPointAndWinGame()
    {
        simpleWinGame("Oliver");
        Assertions.assertEquals(1, match.getPlayerScore("Oliver").getGameScore());
    }

    // Выравнивание счета: 40-AD -> 40-40
    @Test
    void testAddPointAndDeuce()
    {
        matchService.addPoint(id, "Oliver"); // 15-0
        matchService.addPoint(id, "Oliver"); // 30-0
        matchService.addPoint(id, "Oliver"); // 40-0
        matchService.addPoint(id, "Jack"); // 40-15
        matchService.addPoint(id, "Jack"); // 40-30
        matchService.addPoint(id, "Jack"); // 40-40
        matchService.addPoint(id, "Jack"); // 40-AD
        matchService.addPoint(id, "Oliver"); // 40-40
        Assertions.assertEquals("40", match.getPlayerScore("Jack").getPointsScore());
    }

    // Выигрыш гейма после "больше": AD-40 -> win game
    @Test
    void testAddPointToAdvantageAndWinGame()
    {
        matchService.addPoint(id, "Oliver"); // 15-0
        matchService.addPoint(id, "Oliver"); // 30-0
        matchService.addPoint(id, "Oliver"); // 40-0
        matchService.addPoint(id, "Jack"); // 40-15
        matchService.addPoint(id, "Jack"); // 40-30
        matchService.addPoint(id, "Jack"); // 40-40
        matchService.addPoint(id, "Oliver"); // AD-40
        matchService.addPoint(id, "Oliver"); // Win game
        Assertions.assertEquals(1, match.getPlayerScore("Oliver").getGameScore());
    }

    // Обычный выигрыш сета: game 5-4 -> win set
    @Test
    void testWinSet()
    {
        simpleWinSet();
        Assertions.assertEquals(1, match.getPlayerScore("Oliver").getSetScore());
    }

    // Старт тай-брейка: null -> 0
    @Test
    void testStartTieBreak()
    {
        startTieBreak();
        Assertions.assertEquals(0, match.getPlayerScore("Oliver").getTieBreakScore());
    }

    // Выигрыш сета после тай-брейка: 6-5 -> win set
    @Test
    void testWinTieBreak()
    {
        startTieBreak();
        matchService.addPoint(id, "Oliver"); // 1-0
        matchService.addPoint(id, "Oliver"); // 2-0
        matchService.addPoint(id, "Oliver"); // 3-0
        matchService.addPoint(id, "Oliver"); // 4-0
        matchService.addPoint(id, "Oliver"); // 5-0
        matchService.addPoint(id, "Oliver"); // 6-0
        matchService.addPoint(id, "Jack"); // 6-1
        matchService.addPoint(id, "Jack"); // 6-2
        matchService.addPoint(id, "Jack"); // 6-3
        matchService.addPoint(id, "Jack"); // 6-4
        matchService.addPoint(id, "Jack"); // 6-5
        matchService.addPoint(id, "Oliver"); // Win set
        Assertions.assertEquals(1, match.getPlayerScore("Oliver").getSetScore());
    }

    // Розыгрыш сета после тай-брейка: win set -> tieBreak = null
    @Test
    void testEndTieBreak()
    {
        startTieBreak();
        matchService.addPoint(id, "Oliver"); // 1-0
        matchService.addPoint(id, "Oliver"); // 2-0
        matchService.addPoint(id, "Oliver"); // 3-0
        matchService.addPoint(id, "Oliver"); // 4-0
        matchService.addPoint(id, "Oliver"); // 5-0
        matchService.addPoint(id, "Oliver"); // 6-0
        matchService.addPoint(id, "Jack"); // 6-1
        matchService.addPoint(id, "Jack"); // 6-2
        matchService.addPoint(id, "Jack"); // 6-3
        matchService.addPoint(id, "Jack"); // 6-4
        matchService.addPoint(id, "Jack"); // 6-5
        matchService.addPoint(id, "Oliver"); // Win set
        matchService.addPoint(id, "Oliver"); // 15-0 in a new set
        Assertions.assertEquals("15", match.getPlayerScore("Oliver").getPointsScore());
    }

    // Выигрыш матча после двух сетов: 1-0 -> win match
    @Test
    void testHaveWinner()
    {
        simpleWinSet();
        simpleWinSet();
        Assertions.assertEquals("Oliver", match.getWinnerName());
    }

    // Неизвестное имя при попытке добавления очка
    @Test
    void testUnknownName()
    {
        InvalidValueException exception = Assertions.assertThrows(InvalidValueException.class, () -> {
            matchService.addPoint(id, "Alice");
        });
        Assertions.assertEquals("Игрок с таким именем не найден.", exception.getMessage());
    }

    // Попытка начислить очко после победы
    @Test
    void testAlreadyHaveWinner()
    {
        simpleWinSet();
        simpleWinSet(); // winner - Oliver
        matchService.addPoint(id, "Oliver");
        Assertions.assertEquals("0", match.getPlayerScore("Oliver").getPointsScore());
    }

    void simpleWinGame(String name) {
        matchService.addPoint(id, name); // 15
        matchService.addPoint(id, name); // 30
        matchService.addPoint(id, name); // 40
        matchService.addPoint(id, name); // Win game
    }

    void simpleWinSet() {
        simpleWinGame("Oliver"); // game 1-0
        simpleWinGame("Oliver"); // game 2-0
        simpleWinGame("Oliver"); // game 3-0
        simpleWinGame("Oliver"); // game 4-0
        simpleWinGame("Oliver"); // game 5-0
        simpleWinGame("Jack"); // game 5-1
        simpleWinGame("Jack"); // game 5-2
        simpleWinGame("Jack"); // game 5-3
        simpleWinGame("Jack"); // game 5-4
        simpleWinGame("Oliver"); // Win set
    }

    void startTieBreak() {
        simpleWinGame("Oliver"); // game 1-0
        simpleWinGame("Oliver"); // game 2-0
        simpleWinGame("Oliver"); // game 3-0
        simpleWinGame("Oliver"); // game 4-0
        simpleWinGame("Oliver"); // game 5-0
        simpleWinGame("Jack"); // game 5-1
        simpleWinGame("Jack"); // game 5-2
        simpleWinGame("Jack"); // game 5-3
        simpleWinGame("Jack"); // game 5-4
        simpleWinGame("Jack"); // game 5-5
        simpleWinGame("Jack"); // game 5-6
        simpleWinGame("Oliver"); // Start tie-break
    }
}
