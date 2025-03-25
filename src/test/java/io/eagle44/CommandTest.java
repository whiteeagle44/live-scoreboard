package io.eagle44;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {
    private static final String START_COMMAND = "start";
    private static final String UPDATE_COMMAND = "update";
    private static final String FINISH_COMMAND = "finish";
    private static final String SUMMARY_COMMAND = "summary";

    @Test
    @DisplayName("Should create start game command with valid parameters")
    void shouldCreateStartGameCommandWithValidParameters() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;

        // When
        Command command = Command.create(START_COMMAND, homeTeam, awayTeam);

        // Then
        assertEquals(START_COMMAND, command.getType());
        assertEquals(2, command.getArgs().length);
        assertEquals(homeTeam, command.getArgs()[0]);
        assertEquals(awayTeam, command.getArgs()[1]);
    }

    @Test
    @DisplayName("Should create update score command with valid parameters")
    void shouldCreateUpdateScoreCommandWithValidParameters() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;
        String homeScore = String.valueOf(TestFixtures.SAMPLE_HOME_SCORE);
        String awayScore = String.valueOf(TestFixtures.SAMPLE_AWAY_SCORE);

        // When
        Command command = Command.create(UPDATE_COMMAND, homeTeam, awayTeam, homeScore, awayScore);

        // Then
        assertEquals(UPDATE_COMMAND, command.getType());
        assertEquals(4, command.getArgs().length);
        assertEquals(homeTeam, command.getArgs()[0]);
        assertEquals(awayTeam, command.getArgs()[1]);
        assertEquals(homeScore, command.getArgs()[2]);
        assertEquals(awayScore, command.getArgs()[3]);
    }

    @Test
    @DisplayName("Should create finish game command with valid parameters")
    void shouldCreateFinishGameCommandWithValidParameters() {
        // Given
        String homeTeam = TestFixtures.SAMPLE_HOME_TEAM;
        String awayTeam = TestFixtures.SAMPLE_AWAY_TEAM;

        // When
        Command command = Command.create(FINISH_COMMAND, homeTeam, awayTeam);

        // Then
        assertEquals(FINISH_COMMAND, command.getType());
        assertEquals(2, command.getArgs().length);
        assertEquals(homeTeam, command.getArgs()[0]);
        assertEquals(awayTeam, command.getArgs()[1]);
    }

    @Test
    @DisplayName("Should create summary command with no arguments")
    void shouldCreateSummaryCommandWithNoArguments() {
        // When
        Command command = Command.create(SUMMARY_COMMAND);

        // Then
        assertEquals(SUMMARY_COMMAND, command.getType());
        assertEquals(0, command.getArgs().length);
    }

    // Todo: fix
    @Disabled
    @ParameterizedTest
    @DisplayName("Should throw exception for invalid command")
    @MethodSource("invalidCommandProvider")
    void shouldThrowExceptionForInvalidCommand(String type, String[] args, String expectedMessageContains) {
        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Command.create(type, args)
        );
        assertTrue(exception.getMessage().contains(expectedMessageContains),
                "Expected message to contain '" + expectedMessageContains + "' but was '" + exception.getMessage() + "'");
    }

    private static Stream<Arguments> invalidCommandProvider() {{
            return Stream.of(
                    Arguments.of(null, new String[]{"arg1"}, "Command type cannot be null"),
                    Arguments.of("invalid", new String[]{}, "Invalid command type"),
                    // Use empty array instead of null for cases that need to test argument validation
                    Arguments.of(START_COMMAND, new String[0], "Invalid number of arguments"),
                    Arguments.of(START_COMMAND, new String[]{TestFixtures.SAMPLE_HOME_TEAM}, "Invalid number of arguments"),
                    Arguments.of(UPDATE_COMMAND, new String[]{}, "Invalid number of arguments"),
                    Arguments.of(UPDATE_COMMAND, new String[]{TestFixtures.SAMPLE_HOME_TEAM}, "Invalid number of arguments"),
                    Arguments.of(UPDATE_COMMAND, new String[]{TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM}, "Invalid number of arguments"),
                    Arguments.of(UPDATE_COMMAND, new String[]{TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM, "invalid", "1"}, "Invalid score format"),
                    Arguments.of(FINISH_COMMAND, new String[]{}, "Invalid number of arguments"),
                    Arguments.of(FINISH_COMMAND, new String[]{TestFixtures.SAMPLE_HOME_TEAM}, "Invalid number of arguments"),
                    Arguments.of(SUMMARY_COMMAND, new String[]{"extra"}, "Invalid number of arguments")
            );
        }
    }

    @Test
    @DisplayName("Should handle case insensitive command types")
    void shouldHandleCaseInsensitiveCommandTypes() {
        // When
        Command command = Command.create(START_COMMAND.toUpperCase(), TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);

        // Then
        assertEquals(START_COMMAND, command.getType());
    }

    @Test
    @DisplayName("Should validate command arguments")
    void shouldValidateCommandArguments() {
        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Command.create(UPDATE_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM, "invalid", String.valueOf(TestFixtures.SAMPLE_AWAY_SCORE))
        );
        assertTrue(exception.getMessage().contains("Invalid score format"));
    }

    @Test
    @DisplayName("Should properly implement equals and hashCode")
    void shouldImplementEqualsAndHashCode() {
        // Given
        Command command1 = Command.create(START_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);
        Command command2 = Command.create(START_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);
        Command differentCommand = Command.create(FINISH_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);

        // Then
        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
        assertNotEquals(command1, differentCommand);
    }

    @Test
    @DisplayName("Should provide string representation")
    void shouldProvideStringRepresentation() {
        // Given
        Command command = Command.create(START_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);

        // When
        String toString = command.toString();

        // Then
        assertTrue(toString.contains(START_COMMAND));
        assertTrue(toString.contains(TestFixtures.SAMPLE_HOME_TEAM));
        assertTrue(toString.contains(TestFixtures.SAMPLE_AWAY_TEAM));
    }
}