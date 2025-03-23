# Live Scoreboard Application

A Java 21 console application for managing live sports scores with real-time updates and sorting capabilities.

## Requirements

- Java 21 or higher
- Gradle 8.0 or higher

## Project Structure

```
src/
├── main/java/io/eagle44/
│   ├── Game.java
│   ├── Scoreboard.java
│   ├── Country.java
│   ├── CountryValidator.java
│   ├── Command.java
│   ├── CommandType.java
│   ├── ConsoleInput.java
│   └── ConsoleOutput.java
└── test/java/io/eagle44/
    ├── GameTest.java
    ├── ScoreboardTest.java
    ├── CountryTest.java
    ├── CountryValidatorTest.java
    ├── CommandTest.java
    ├── ConsoleInputOutputTest.java
    └── LiveScoreboardItTest.java
```

## Features

1. Start a game
2. Finish a game
3. Update score
4. Get summary of games ordered by total score and recency

## Architecture

This project follows clean architecture principles with a focus on simplicity:

### Core Components
- `Game`: Represents a game with its state and rules
- `Scoreboard`: Manages the collection of games and their operations
- `Country`: Represents a country with validation rules
- `Command`: Handles user input parsing and validation

### User Interface
- `ConsoleInput`: Handles user input from the console
- `ConsoleOutput`: Manages output to the console

## Assumptions and Validation Rules

### Country Names
- Must be a valid country name from a predefined list
- Cannot contain consecutive spaces
- Examples of valid names:
  - "Spain"
  - "United States"
  - "United Kingdom"
- Examples of invalid names:
  - "Sp@in" (not from predefined list)
  - "United     States" (consecutive spaces)

### Scores
- Must be non-negative integers
- No maximum limit (practical limit based on integer type)
- Cannot be updated for finished games

### Game Management
- Each game has a unique ID (UUID)
- Games cannot be started with the same country names
- Games cannot be finished twice
- Finished games are stored in a separate collection
- Games are sorted by:
  1. Total score (highest first)
  2. Start time (most recent first)

### User Input Validation
- Commands must be in the correct format
- Country names must be valid
- Scores must be valid integers
- Game IDs must be valid UUIDs
- Appropriate error messages for invalid inputs

## Building and Testing

To build the project:
```bash
./gradlew build
```

To run tests:
```bash
./gradlew test
```

To generate test coverage report:
```bash
./gradlew jacocoTestReport
```

## Development

This project follows Test-Driven Development (TDD) practices with a focus on clean code principles.

### Test Structure
1. Integration Tests (ItTest suffix)
   - Test complete user flows
   - Include presentation layer
   - Test multiple components together
   - Example: `LiveScoreboardItTest.java`

2. Unit Tests
   - Test individual components
   - Focus on single method functionality
   - Example: `GameTest.java`, `ScoreboardTest.java`

### Code Style
- Clean Code principles
- SOLID design principles
- Immutable data structures where possible
- Clear and descriptive method names
- Comprehensive documentation 
