package io.eagle44;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {
    private static final String ERROR_GAME_EXISTS = """
        Game already exists between %s and %s.
        Please finish the current game before starting a new one.
        """;
    private static final String ERROR_GAME_NOT_FOUND = """
        No active game found between %s and %s.
        Please start a game first.
        """;
    private static final String ERROR_GAME_FINISHED = """
        Game between %s and %s has already finished.
        Please start a new game to update scores.
        """;
    private static final String ERROR_INVALID_SCORE = "Invalid score format";
    private static final String ERROR_NEGATIVE_SCORE = "Score cannot be negative";
    private static final String ERROR_SAME_TEAM = "Home and away teams cannot be the same";

    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    @DisplayName("Should start new game")
    void shouldStartNewGame() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;

        // When
        Game game = scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam));

        // Then
        assertNotNull(game);
        assertEquals(Country.of(homeTeam), game.getHomeTeam());
        assertEquals(Country.of(awayTeam), game.getAwayTeam());
        assertEquals(0, game.getHomeScore());
        assertEquals(0, game.getAwayScore());
    }

    @Test
    @DisplayName("Should update game score")
    void shouldUpdateGameScore() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        Game game = scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam));

        // When
        scoreboard.updateScore(Country.of(homeTeam), Country.of(awayTeam), 2, 1);

        // Then
        assertEquals(2, game.getHomeScore());
        assertEquals(1, game.getAwayScore());
    }

    @Test
    @DisplayName("Should finish game")
    void shouldFinishGame() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam));

        // When
        scoreboard.finishGame(Country.of(homeTeam), Country.of(awayTeam));

        // Then
        // TODO: fix
//        assertFalse(scoreboard.getGameSummary().contains(homeTeam + " vs " + awayTeam));
    }

    @Test
    @DisplayName("Should get game summary")
    void shouldGetGameSummary() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam));
        scoreboard.updateScore(Country.of(homeTeam), Country.of(awayTeam), 2, 1);

        // When
        List<Game> summary = scoreboard.getGameSummary();

        // Then
        assertFalse(summary.isEmpty());
        assertEquals(1, summary.size());
        Game game = summary.getFirst();
        assertEquals(Country.of(homeTeam), game.getHomeTeam());
        assertEquals(Country.of(awayTeam), game.getAwayTeam());
        assertEquals(2, game.getHomeScore());
        assertEquals(1, game.getAwayScore());
    }

    @Test
    @DisplayName("Should throw exception when starting duplicate game")
    void shouldThrowExceptionWhenStartingDuplicateGame() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam));

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam)));
        assertEquals("Game already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent game")
    void shouldThrowExceptionWhenUpdatingNonExistentGame() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scoreboard.updateScore(Country.of(homeTeam), Country.of(awayTeam), 1, 0));
        assertEquals("Game not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when finishing non-existent game")
    void shouldThrowExceptionWhenFinishingNonExistentGame() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;

        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scoreboard.finishGame(Country.of(homeTeam), Country.of(awayTeam)));
        assertEquals("Game not found", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid team names")
    @MethodSource("invalidTeamNamesProvider")
    void shouldThrowExceptionForInvalidTeamNames(String homeTeam, String awayTeam, String expectedMessage) {
        // When/Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam)));
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> invalidTeamNamesProvider() {
        return Stream.of(
            Arguments.of(null, "France", "Home team name cannot be null"),
            Arguments.of("Spain", null, "Away team name cannot be null"),
            Arguments.of("", "France", "Home team name cannot be empty"),
            Arguments.of("Spain", "", "Away team name cannot be empty"),
            Arguments.of("Spain", "Spain", "Home and away teams cannot be the same")
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid scores")
    @MethodSource("invalidScoresProvider")
    void shouldThrowExceptionForInvalidScores(int homeScore, int awayScore, String expectedMessage) {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        scoreboard.startGame(Country.of(homeTeam), Country.of(awayTeam));

        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> scoreboard.updateScore(Country.of(homeTeam), Country.of(awayTeam), homeScore, awayScore)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> invalidScoresProvider() {
        return Stream.of(
            Arguments.of(-1, 0, ERROR_NEGATIVE_SCORE),
            Arguments.of(0, -1, ERROR_NEGATIVE_SCORE),
            Arguments.of(-1, -1, ERROR_NEGATIVE_SCORE)
        );
    }
} 