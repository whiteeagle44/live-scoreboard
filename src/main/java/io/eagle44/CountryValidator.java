package io.eagle44;

public final class CountryValidator {
    public boolean isValid(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            return false;
        }

        return AcceptedCountries.contains(countryName.trim());
    }
}
