package io.eagle44;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    @Test
    @DisplayName("Should create country with valid name")
    void shouldCreateCountryWithValidName() {
        // Given
        String name = TestFixtures.VALID_COUNTRIES.get(0);

        // When
        Country country = new Country(name);

        // Then
        assertEquals(name, country.name());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid country name")
    @MethodSource("invalidNameProvider")
    void shouldThrowExceptionForInvalidCountryName(String name, String expectedMessage) {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Country(name)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
            Arguments.of(null, "Country name cannot be null"),
            Arguments.of("", "Country name cannot be empty"),
            Arguments.of(" ", "Country name cannot be empty"),
            Arguments.of("   ", "Country name cannot be empty"),
            Arguments.of(TestFixtures.COUNTRY_WITH_SPECIAL_CHARS, "Country name contains invalid characters"),
            Arguments.of(TestFixtures.COUNTRY_EXCEEDING_MAX_LENGTH, "Country name exceeds maximum length of 50 characters")
        );
    }

    @Test
    @DisplayName("Should normalize country name")
    void shouldNormalizeCountryName() {
        // Given
        String name = "  Spain  ";

        // When
        Country country = new Country(name);

        // Then
        assertEquals("Spain", country.name());
    }

    @Test
    @DisplayName("Should handle case insensitive country names")
    void shouldHandleCaseInsensitiveCountryNames() {
        // Given
        String name = "SPAIN";

        // When
        Country country = new Country(name);

        // Then
        assertEquals("Spain", country.name());
    }

    @Test
    @DisplayName("Should validate against predefined country list")
    void shouldValidateAgainstPredefinedCountryList() {
        // Given
        String name = TestFixtures.INVALID_COUNTRY;

        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Country(name)
        );
        assertEquals("Invalid country name: " + name, exception.getMessage());
    }

    @Test
    @DisplayName("Should handle special characters in country names")
    void shouldHandleSpecialCharactersInCountryNames() {
        // Given
        String name = TestFixtures.COUNTRY_WITH_SPECIAL_CHARS;

        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Country(name)
        );
        assertEquals("Country name contains invalid characters", exception.getMessage());
    }
} 