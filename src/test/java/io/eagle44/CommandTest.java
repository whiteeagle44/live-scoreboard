package io.eagle44;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    private static final String START_COMMAND = "start";
    private static final String UPDATE_COMMAND = "update";
    private static final String FINISH_COMMAND = "finish";
    private static final String SUMMARY_COMMAND = "summary";
    
    private static final String ERROR_INVALID_COMMAND_TYPE = """
        Invalid command type. Available commands are:
        - start <home> <away>
        - update <home> <away> <homeScore> <awayScore>
        - finish <home> <away>
        - summary
        """;
    private static final String ERROR_INVALID_ARGUMENTS = "Invalid command arguments";
    private static final String ERROR_INVALID_SCORE = "Invalid score format";

    @Test
    @DisplayName("Should create start game command with valid parameters")
    void shouldCreateStartGameCommandWithValidParameters() {
        // Given
        List<String> args = List.of(START_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);

        // When
        Command command = Command.fromArgs(args);

        // Then
        assertEquals(CommandType.START, command.type());
        assertEquals(TestFixtures.SAMPLE_HOME_TEAM, command.homeTeam());
        assertEquals(TestFixtures.SAMPLE_AWAY_TEAM, command.awayTeam());
        assertNull(command.homeScore());
        assertNull(command.awayScore());
    }

    @Test
    @DisplayName("Should create update score command with valid parameters")
    void shouldCreateUpdateScoreCommandWithValidParameters() {
        // Given
        List<String> args = List.of(
            UPDATE_COMMAND,
            TestFixtures.SAMPLE_HOME_TEAM,
            TestFixtures.SAMPLE_AWAY_TEAM,
            String.valueOf(TestFixtures.SAMPLE_HOME_SCORE),
            String.valueOf(TestFixtures.SAMPLE_AWAY_SCORE)
        );

        // When
        Command command = Command.fromArgs(args);

        // Then
        assertEquals(CommandType.UPDATE, command.type());
        assertEquals(TestFixtures.SAMPLE_HOME_TEAM, command.homeTeam());
        assertEquals(TestFixtures.SAMPLE_AWAY_TEAM, command.awayTeam());
        assertEquals(TestFixtures.SAMPLE_HOME_SCORE, command.homeScore());
        assertEquals(TestFixtures.SAMPLE_AWAY_SCORE, command.awayScore());
    }

    @Test
    @DisplayName("Should create finish game command with valid parameters")
    void shouldCreateFinishGameCommandWithValidParameters() {
        // Given
        List<String> args = List.of(FINISH_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);

        // When
        Command command = Command.fromArgs(args);

        // Then
        assertEquals(CommandType.FINISH, command.type());
        assertEquals(TestFixtures.SAMPLE_HOME_TEAM, command.homeTeam());
        assertEquals(TestFixtures.SAMPLE_AWAY_TEAM, command.awayTeam());
        assertNull(command.homeScore());
        assertNull(command.awayScore());
    }

    @Test
    @DisplayName("Should create summary command with no arguments")
    void shouldCreateSummaryCommandWithNoArguments() {
        // Given
        List<String> args = List.of(SUMMARY_COMMAND);

        // When
        Command command = Command.fromArgs(args);

        // Then
        assertEquals(CommandType.SUMMARY, command.type());
        assertNull(command.homeTeam());
        assertNull(command.awayTeam());
        assertNull(command.homeScore());
        assertNull(command.awayScore());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid command")
    @MethodSource("invalidCommandProvider")
    void shouldThrowExceptionForInvalidCommand(List<String> args, String expectedMessage) {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Command.fromArgs(args)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> invalidCommandProvider() {
        return Stream.of(
            Arguments.of(List.of(), ERROR_INVALID_COMMAND_TYPE),
            Arguments.of(List.of((String)null), ERROR_INVALID_COMMAND_TYPE),
            Arguments.of(List.of("invalid"), ERROR_INVALID_COMMAND_TYPE),
            Arguments.of(List.of(START_COMMAND), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(START_COMMAND, TestFixtures.SAMPLE_HOME_TEAM), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(UPDATE_COMMAND), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(UPDATE_COMMAND, TestFixtures.SAMPLE_HOME_TEAM), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(UPDATE_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(UPDATE_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM, "invalid"), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(FINISH_COMMAND), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(FINISH_COMMAND, TestFixtures.SAMPLE_HOME_TEAM), ERROR_INVALID_ARGUMENTS),
            Arguments.of(List.of(SUMMARY_COMMAND, "extra"), ERROR_INVALID_ARGUMENTS)
        );
    }

    @Test
    @DisplayName("Should handle case insensitive command types")
    void shouldHandleCaseInsensitiveCommandTypes() {
        // Given
        List<String> args = List.of(START_COMMAND.toUpperCase(), TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);

        // When
        Command command = Command.fromArgs(args);

        // Then
        assertEquals(CommandType.START, command.type());
    }

    @Test
    @DisplayName("Should validate command arguments")
    void shouldValidateCommandArguments() {
        // Given
        List<String> args = List.of(
            UPDATE_COMMAND,
            TestFixtures.SAMPLE_HOME_TEAM,
            TestFixtures.SAMPLE_AWAY_TEAM,
            "invalid",
            String.valueOf(TestFixtures.SAMPLE_AWAY_SCORE)
        );

        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Command.fromArgs(args)
        );
        assertEquals(ERROR_INVALID_SCORE, exception.getMessage());
    }
} 