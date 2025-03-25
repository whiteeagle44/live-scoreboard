package io.eagle44;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    @DisplayName("Should create game with valid parameters")
    void shouldCreateGameWithValidParameters() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        int homeScore = TestFixtures.SAMPLE_HOME_SCORE;
        int awayScore = TestFixtures.SAMPLE_AWAY_SCORE;

        // When
        Game game = new Game(Country.of(homeTeam), Country.of(awayTeam), homeScore, awayScore);

        // Then
        assertEquals(Country.of(homeTeam), game.getHomeTeam());
        assertEquals(Country.of(awayTeam), game.getAwayTeam());
        assertEquals(homeScore, game.getHomeScore());
        assertEquals(awayScore, game.getAwayScore());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid parameters")
    @MethodSource("invalidParametersProvider")
    void shouldThrowExceptionForInvalidParameters(String homeTeam, String awayTeam, int homeScore, int awayScore, String expectedMessage) {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Game(Country.of(homeTeam), Country.of(awayTeam), homeScore, awayScore)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> invalidParametersProvider() {
        return Stream.of(
            Arguments.of(null, TestFixtures.SAMPLE_AWAY_TEAM, 0, 0, "Home team cannot be null"),
            Arguments.of(TestFixtures.SAMPLE_HOME_TEAM, null, 0, 0, "Away team cannot be null"),
            Arguments.of("", TestFixtures.SAMPLE_AWAY_TEAM, 0, 0, "Home team cannot be empty"),
            Arguments.of(TestFixtures.SAMPLE_HOME_TEAM, "", 0, 0, "Away team cannot be empty"),
            Arguments.of(TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM, -1, 0, "Home score cannot be negative"),
            Arguments.of(TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM, 0, -1, "Away score cannot be negative")
        );
    }

    @Test
    @DisplayName("Should calculate total score correctly")
    void shouldCalculateTotalScoreCorrectly() {
        // Given
        Game game = new Game(Country.of(TestFixtures.SAMPLE_HOME_TEAM), Country.of(TestFixtures.SAMPLE_AWAY_TEAM), TestFixtures.SAMPLE_HOME_SCORE, TestFixtures.SAMPLE_AWAY_SCORE);

        // When
        int totalScore = game.getTotalScore();

        // Then
        assertEquals(TestFixtures.SAMPLE_HOME_SCORE + TestFixtures.SAMPLE_AWAY_SCORE, totalScore);
    }

} 