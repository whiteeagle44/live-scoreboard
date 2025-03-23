package io.eagle44;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Game {
    private final Country homeTeam;
    private final Country awayTeam;
    private final int homeScore;
    private final int awayScore;
    private final LocalDateTime startTime;

    private Game(Country homeTeam, Country awayTeam, int homeScore, int awayScore, LocalDateTime startTime) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.startTime = startTime;
    }

    public static Game start(Country homeTeam, Country awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Teams cannot be null");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home and away teams cannot be the same");
        }
        return new Game(homeTeam, awayTeam, 0, 0, LocalDateTime.now());
    }

    public Game updateScore(int newHomeScore, int newAwayScore) {
        if (newHomeScore < 0 || newAwayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        return new Game(homeTeam, awayTeam, newHomeScore, newAwayScore, startTime);
    }

    public Country getHomeTeam() {
        return homeTeam;
    }

    public Country getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return homeTeam.equals(game.homeTeam) && awayTeam.equals(game.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam);
    }

    @Override
    public String toString() {
        return String.format("%s %d - %d %s", homeTeam, homeScore, awayScore, awayTeam);
    }
} 