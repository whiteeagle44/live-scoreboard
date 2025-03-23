package io.eagle44;

import java.util.Arrays;
import java.util.Objects;

public final class Command {
    private final String type;
    private final String[] args;

    private Command(String type, String[] args) {
        this.type = type;
        this.args = args;
    }

    public static Command create(String type, String... args) {
        Objects.requireNonNull(type, "Command type cannot be null");
        type = type.trim().toLowerCase();
        
        switch (type) {
            case "start":
                validateStartCommand(args);
                break;
            case "update":
                validateUpdateCommand(args);
                break;
            case "finish":
                validateFinishCommand(args);
                break;
            case "summary":
                validateSummaryCommand(args);
                break;
            default:
                throw new IllegalArgumentException("Invalid command type: " + type);
        }

        return new Command(type, args);
    }

    private static void validateStartCommand(String[] args) {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("Invalid number of arguments for start command");
        }
    }

    private static void validateUpdateCommand(String[] args) {
        if (args == null || args.length != 4) {
            throw new IllegalArgumentException("Invalid number of arguments for update command");
        }
        try {
            Integer.parseInt(args[2]);
            Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid score format");
        }
    }

    private static void validateFinishCommand(String[] args) {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("Invalid number of arguments for finish command");
        }
    }

    private static void validateSummaryCommand(String[] args) {
        if (args != null && args.length > 0) {
            throw new IllegalArgumentException("Invalid number of arguments for summary command");
        }
    }

    public String getType() {
        return type;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return type.equals(command.type) && Arrays.equals(args, command.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, Arrays.hashCode(args));
    }

    @Override
    public String toString() {
        return type + " " + String.join(" ", args);
    }
} 