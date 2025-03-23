package io.eagle44;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputOutputTest {
    private static final String START_COMMAND = "start";
    private static final String UPDATE_COMMAND = "update";
    private static final String FINISH_COMMAND = "finish";
    private static final String SUMMARY_COMMAND = "summary";
    
    private static final String WELCOME_MESSAGE = """
        Welcome to Live Scoreboard
        Available commands:
        - start <home> <away>
        - update <home> <away> <homeScore> <awayScore>
        - finish <home> <away>
        - summary
        """;
    private static final String COMMAND_PROMPT = "> ";
    private static final String NEWLINE = "\n";
    private static final String GAME_SUMMARY_HEADER = "Game Summary:";
    private static final String NO_GAMES_MESSAGE = "No games in progress";
    private static final String ERROR_PREFIX = "Error: ";
    
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ConsoleInput consoleInput;
    private ConsoleOutput consoleOutput;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        consoleOutput = TestUtils.createConsoleOutput(outputStream);
    }

    @AfterEach
    void tearDown() {
        TestUtils.restoreSystemOut(originalOut);
    }

    @Test
    @DisplayName("Should read command from console")
    void shouldReadCommandFromConsole() {
        // Given
        List<String> args = List.of(START_COMMAND, TestFixtures.SAMPLE_HOME_TEAM, TestFixtures.SAMPLE_AWAY_TEAM);
        consoleInput = TestUtils.createConsoleInput(TestUtils.formatCommandWithNewline(args));

        // When
        String command = consoleInput.readCommand();

        // Then
        assertEquals(TestUtils.formatCommand(args), command);
    }

    @Test
    @DisplayName("Should handle empty input")
    void shouldHandleEmptyInput() {
        // Given
        consoleInput = TestUtils.createConsoleInput(NEWLINE);

        // When
        String command = consoleInput.readCommand();

        // Then
        assertEquals("", command);
    }

    @Test
    @DisplayName("Should display welcome message")
    void shouldDisplayWelcomeMessage() {
        // When
        consoleOutput.displayWelcome();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains(WELCOME_MESSAGE));
    }

    @Test
    @DisplayName("Should display error message")
    void shouldDisplayErrorMessage() {
        // Given
        String errorMessage = "Invalid command format";

        // When
        consoleOutput.displayError(errorMessage);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains(ERROR_PREFIX + errorMessage));
    }

    @Test
    @DisplayName("Should display game summary")
    void shouldDisplayGameSummary() {
        // Given
        List<String> games = TestUtils.createGameSummaryList(
            TestUtils.formatGameScore(
                TestFixtures.SAMPLE_HOME_TEAM,
                TestFixtures.SAMPLE_HOME_SCORE,
                TestFixtures.SAMPLE_AWAY_TEAM,
                TestFixtures.SAMPLE_AWAY_SCORE
            ),
            TestUtils.formatGameScore(TestFixtures.VALID_COUNTRIES.get(0), 0, TestFixtures.VALID_COUNTRIES.get(1), 0)
        );

        // When
        consoleOutput.displaySummary(games);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains(GAME_SUMMARY_HEADER));
        assertTrue(output.contains(TestUtils.formatGameScore(
            TestFixtures.SAMPLE_HOME_TEAM,
            TestFixtures.SAMPLE_HOME_SCORE,
            TestFixtures.SAMPLE_AWAY_TEAM,
            TestFixtures.SAMPLE_AWAY_SCORE
        )));
        assertTrue(output.contains(TestUtils.formatGameScore(
            TestFixtures.VALID_COUNTRIES.get(0), 0, TestFixtures.VALID_COUNTRIES.get(1), 0
        )));
    }

    @Test
    @DisplayName("Should display empty game summary")
    void shouldDisplayEmptyGameSummary() {
        // When
        consoleOutput.displaySummary(List.of());

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains(GAME_SUMMARY_HEADER));
        assertTrue(output.contains(NO_GAMES_MESSAGE));
    }

    @Test
    @DisplayName("Should display command prompt")
    void shouldDisplayCommandPrompt() {
        // When
        consoleOutput.displayPrompt();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains(COMMAND_PROMPT));
    }

    @Test
    @DisplayName("Should display game status")
    void shouldDisplayGameStatus() {
        // When
        consoleOutput.displayGameStatus(
            TestFixtures.SAMPLE_HOME_TEAM,
            TestFixtures.SAMPLE_AWAY_TEAM,
            TestFixtures.SAMPLE_HOME_SCORE,
            TestFixtures.SAMPLE_AWAY_SCORE
        );

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains(TestUtils.formatGameScore(
            TestFixtures.SAMPLE_HOME_TEAM,
            TestFixtures.SAMPLE_HOME_SCORE,
            TestFixtures.SAMPLE_AWAY_TEAM,
            TestFixtures.SAMPLE_AWAY_SCORE
        )));
    }
} 