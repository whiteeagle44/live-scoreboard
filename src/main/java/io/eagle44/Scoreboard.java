package io.eagle44;

import java.util.*;
import java.util.stream.Collectors;

public final class Scoreboard {
    private final Map<String, Game> games;

    public Scoreboard() {
        this.games = new HashMap<>();
    }

    public Game startGame(Country homeTeam, Country awayTeam) {
        String gameKey = createGameKey(homeTeam, awayTeam);
        if (games.containsKey(gameKey)) {
            throw new IllegalArgumentException("Game already exists");
        }
        Game game = Game.start(homeTeam, awayTeam);
        games.put(gameKey, game);
        return game;
    }

    public void updateScore(Country homeTeam, Country awayTeam, int homeScore, int awayScore) {
        String gameKey = createGameKey(homeTeam, awayTeam);
        Game existingGame = games.get(gameKey);
        if (existingGame == null) {
            throw new IllegalArgumentException("Game not found");
        }
        games.put(gameKey, existingGame.updateScore(homeScore, awayScore));
    }

    public void finishGame(Country homeTeam, Country awayTeam) {
        String gameKey = createGameKey(homeTeam, awayTeam);
        if (!games.containsKey(gameKey)) {
            throw new IllegalArgumentException("Game not found");
        }
        games.remove(gameKey);
    }

    public List<Game> getGameSummary() {
        return games.values().stream()
            .sorted(Comparator
                .comparing(Game::getTotalScore).reversed()
                .thenComparing(Game::getStartTime).reversed())
            .collect(Collectors.toList());
    }

    private String createGameKey(Country homeTeam, Country awayTeam) {
        return homeTeam.getName() + " vs " + awayTeam.getName();
    }

    public boolean hasGame(Country homeTeam, Country awayTeam) {
        return games.containsKey(createGameKey(homeTeam, awayTeam));
    }
} 