package io.eagle44;

import java.util.Objects;

public final class Country {
    private final String name;

    private Country(String name) {
        this.name = name;
    }

    public static Country of(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }
        return new Country(name.trim());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equalsIgnoreCase(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
} 