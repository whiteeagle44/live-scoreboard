package io.eagle44;

import io.eagle44.console.ConsoleInput;
import io.eagle44.console.ConsoleOutput;
import io.eagle44.command.model.Command;
import io.eagle44.command.model.CommandType;
import io.eagle44.country.model.Country;
import io.eagle44.country.service.CountryValidationService;
import io.eagle44.scoreboard.Scoreboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LiveScoreboardItTest {

    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ConsoleInput consoleInput;
    private ConsoleOutput consoleOutput;
    private CountryValidationService countryValidationService;
    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        Set<String> validCountries = TestFixtures.VALID_COUNTRIES;
        
        countryValidationService = new CountryValidationService(validCountries);
        scoreboard = new Scoreboard();
        consoleInput = new ConsoleInput(new Scanner(System.in));
        consoleOutput = new ConsoleOutput();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should complete full game lifecycle")
    void shouldCompleteFullGameLifecycle() {
        // Given
        String input = String.join("\n", 
            "start Spain France",
            "update Spain France 1 0",
            "update Spain France 2 1",
            "summary",
            "finish Spain France",
            "summary"
        );
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // When
        runApplication();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Live Scoreboard"));
        assertTrue(output.contains("Spain 2 - France 1"));
        assertTrue(output.contains("Game Summary:"));
        assertTrue(output.contains("No games in progress"));
    }

    @Test
    @DisplayName("Should handle multiple concurrent games")
    void shouldHandleMultipleConcurrentGames() {
        // Given
        String input = String.join("\n",
            "start Spain France",
            "start Germany Italy",
            "update Spain France 2 1",
            "update Germany Italy 0 1",
            "summary",
            "finish Spain France",
            "summary"
        );
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // When
        runApplication();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Germany 0 - Italy 1"));
        assertTrue(output.contains("Spain 2 - France 1"));
    }

    @Test
    @DisplayName("Should handle invalid commands gracefully")
    void shouldHandleInvalidCommandsGracefully() {
        // Given
        String input = String.join("\n",
            "invalid",
            "start Spain",
            "update Spain France invalid 1",
            "start Spain France",
            "update Spain France 2 1",
            "summary"
        );
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // When
        runApplication();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Error: Invalid command type"));
        assertTrue(output.contains("Error: Invalid number of arguments for START command"));
        assertTrue(output.contains("Error: Invalid score format"));
        assertTrue(output.contains("Spain 2 - France 1"));
    }

    @Test
    @DisplayName("Should handle invalid country names")
    void shouldHandleInvalidCountryNames() {
        // Given
        String input = String.join("\n",
            "start InvalidCountry France",
            "start Spain InvalidCountry",
            "start Spain France",
            "update Spain France 2 1",
            "summary"
        );
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // When
        runApplication();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Error: Invalid country name"));
        assertTrue(output.contains("Spain 2 - France 1"));
    }

    @Test
    @DisplayName("Should handle duplicate game starts")
    void shouldHandleDuplicateGameStarts() {
        // Given
        String input = String.join("\n",
            "start Spain France",
            "start Spain France",
            "update Spain France 2 1",
            "summary"
        );
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // When
        runApplication();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Error: Game already exists"));
        assertTrue(output.contains("Spain 2 - France 1"));
    }

    @Test
    @DisplayName("Should handle finishing non-existent game")
    void shouldHandleFinishingNonExistentGame() {
        // Given
        String input = String.join("\n",
            "finish Spain France",
            "start Spain France",
            "update Spain France 2 1",
            "finish Spain France",
            "summary"
        );
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // When
        runApplication();

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Error: Game not found"));
        assertTrue(output.contains("No games in progress"));
    }
    
} 