package io.eagle44;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUtils {
    static ConsoleInput createConsoleInput(String input) {
        return new ConsoleInput(new ByteArrayInputStream(input.getBytes()));
    }

    static ConsoleOutput createConsoleOutput(ByteArrayOutputStream outputStream) {
        return new ConsoleOutput(new PrintStream(outputStream));
    }

    static void restoreSystemOut(PrintStream originalOut) {
        System.setOut(originalOut);
    }

    static String formatGameScore(String homeTeam, int homeScore, String awayTeam, int awayScore) {
        return String.format("%s %d - %d %s", homeTeam, homeScore, awayScore, awayTeam);
    }

    static String formatCommand(List<String> args) {
        return String.join(" ", args);
    }

    static String formatCommandWithNewline(List<String> args) {
        return formatCommand(args) + "\n";
    }

    static void assertExceptionMessage(IllegalArgumentException exception, String expectedMessage) {
        assertEquals(expectedMessage, exception.getMessage());
    }
} 