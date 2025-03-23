package io.eagle44;

import java.util.Scanner;
import java.util.List;

public final class LiveScoreboard {
    private final Scoreboard scoreboard;
    private final CountryValidator countryValidator;
    private final Scanner scanner;

    public LiveScoreboard() {
        this.scoreboard = new Scoreboard();
        this.countryValidator = new CountryValidator();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        displayWelcome();
        displayHelp();

        while (true) {
            try {
                displayPrompt();
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                processCommand(input);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void processCommand(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Empty command");
        }

        String commandType = parts[0].toLowerCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        validateCommand(commandType, args);
        Command command = Command.create(commandType, args);
        executeCommand(command);
    }

    private void validateCommand(String commandType, String[] args) {
        switch (commandType) {
            case "start" -> {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for START command");
                }
                validateCountryName(args[0]);
                validateCountryName(args[1]);
            }
            case "update" -> {
                if (args.length != 4) {
                    throw new IllegalArgumentException("Invalid number of arguments for UPDATE command");
                }
                validateCountryName(args[0]);
                validateCountryName(args[1]);
                validateScore(args[2]);
                validateScore(args[3]);
            }
            case "finish" -> {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for FINISH command");
                }
                validateCountryName(args[0]);
                validateCountryName(args[1]);
            }
            case "summary" -> {
                if (args.length != 0) {
                    throw new IllegalArgumentException("Invalid number of arguments for SUMMARY command");
                }
            }
            default -> throw new IllegalArgumentException("Invalid command type");
        }
    }

    private void validateCountryName(String countryName) {
        if (!countryValidator.isValid(countryName)) {
            throw new IllegalArgumentException("Invalid country name");
        }
    }

    private void validateScore(String score) {
        try {
            int value = Integer.parseInt(score);
            if (value < 0) {
                throw new IllegalArgumentException("Score cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid score format");
        }
    }

    private void executeCommand(Command command) {
        switch (command.getType()) {
            case "start" -> startGame(command);
            case "update" -> updateScore(command);
            case "finish" -> finishGame(command);
            case "summary" -> displaySummary();
            default -> throw new IllegalArgumentException("Unknown command type: " + command.getType());
        }
    }

    private void startGame(Command command) {
        String[] args = command.getArgs();
        Country homeTeam = Country.of(args[0]);
        Country awayTeam = Country.of(args[1]);
        
        if (scoreboard.hasGame(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Game already exists");
        }
        
        Game game = scoreboard.startGame(homeTeam, awayTeam);
        System.out.println("Game started: " + game);
    }

    private void updateScore(Command command) {
        String[] args = command.getArgs();
        Country homeTeam = Country.of(args[0]);
        Country awayTeam = Country.of(args[1]);
        int homeScore = Integer.parseInt(args[2]);
        int awayScore = Integer.parseInt(args[3]);
        
        if (!scoreboard.hasGame(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Game not found");
        }
        
        scoreboard.updateScore(homeTeam, awayTeam, homeScore, awayScore);
        System.out.println("Score updated: " + homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam);
    }

    private void finishGame(Command command) {
        String[] args = command.getArgs();
        Country homeTeam = Country.of(args[0]);
        Country awayTeam = Country.of(args[1]);
        
        if (!scoreboard.hasGame(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Game not found");
        }
        
        scoreboard.finishGame(homeTeam, awayTeam);
        System.out.println("Game finished: " + homeTeam + " vs " + awayTeam);
    }

    private void displaySummary() {
        List<Game> games = scoreboard.getGameSummary();
        if (games.isEmpty()) {
            System.out.println("No games in progress");
            return;
        }

        System.out.println("Game Summary:");
        games.forEach(game -> System.out.println(game));
    }

    private void displayWelcome() {
        System.out.println("Welcome to Live Scoreboard");
        System.out.println("------------------------");
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  start <home_team> <away_team>");
        System.out.println("  update <home_team> <away_team> <home_score> <away_score>");
        System.out.println("  finish <home_team> <away_team>");
        System.out.println("  summary");
        System.out.println("  exit");
        System.out.println("------------------------");
    }

    private void displayPrompt() {
        System.out.print("> ");
    }

    public static void main(String[] args) {
        new LiveScoreboard().start();
    }
} 