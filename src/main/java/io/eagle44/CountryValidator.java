package io.eagle44;

import java.util.Objects;

public final class CountryValidator {
    public void validate(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid country name: " + countryName + ". Please use one of the predefined country names.");
        }

        String normalizedName = countryName.trim();
        if (!AcceptedCountries.contains(normalizedName)) {
            throw new IllegalArgumentException("Invalid country name: " + countryName + ". Please use one of the predefined country names.");
        }
    }

    public boolean isValid(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            return false;
        }

        return AcceptedCountries.contains(countryName.trim());
    }
} 