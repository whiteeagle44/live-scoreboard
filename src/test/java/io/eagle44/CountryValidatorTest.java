package io.eagle44;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CountryValidatorTest {
    private static final String ERROR_INVALID_COUNTRY = """
        Invalid country name: %s. Accepted country names are:
        "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan",
        "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
        "Cambodia", "Cameroon", "Canada", "Cape Verde", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
        "Denmark", "Djibouti", "Dominica", "Dominican Republic",
        "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia",
        "Fiji", "Finland", "France",
        "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",
        "Haiti", "Honduras", "Hungary",
        "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Ivory Coast",
        "Jamaica", "Japan", "Jordan",
        "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan",
        "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
        "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar",
        "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea", "North Macedonia", "Norway",
        "Oman",
        "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal",
        "Qatar",
        "Romania", "Russia", "Rwanda",
        "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria",
        "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu",
        "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan",
        "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
        "Yemen",
        "Zambia", "Zimbabwe"
    """;

    private CountryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CountryValidator();
    }

    @Test
    @DisplayName("Should validate country name from predefined list")
    void shouldValidateCountryNameFromPredefinedList() {
        // Given
        String name = TestFixtures.VALID_COUNTRIES.getFirst();

        // When/Then
        assertDoesNotThrow(() -> validator.isValid(name));
    }

    @Test
    @DisplayName("Should handle case insensitive validation")
    void shouldHandleCaseInsensitiveValidation() {
        // Given
        String name = TestFixtures.VALID_COUNTRIES.getFirst().toUpperCase();

        // When/Then
        assertDoesNotThrow(() -> validator.isValid(name));
    }

    @Test
    @DisplayName("Should handle whitespace in country names")
    void shouldHandleWhitespaceInCountryNames() {
        // Given
        String name = "  " + TestFixtures.VALID_COUNTRIES.getFirst() + "  ";

        // When/Then
        assertDoesNotThrow(() -> validator.isValid(name));
    }

    @ParameterizedTest
    @DisplayName("Should reject invalid country names")
    @ValueSource(strings = {
        "InvalidCountry",
        "United  States",
        "123",
        "Country123",
        "Country@#$",
        "Country Country",
        "C",
    })
    void shouldRejectInvalidCountryNames(String name) {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.isValid(name)
        );
        assertEquals(ERROR_INVALID_COUNTRY.formatted(name), exception.getMessage());
    }

    @Test
    @DisplayName("Should reject null country name")
    void shouldRejectNullCountryName() {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.isValid(null)
        );
        assertEquals(ERROR_INVALID_COUNTRY.formatted("null"), exception.getMessage());
    }

    @Test
    @DisplayName("Should reject empty country name")
    void shouldRejectEmptyCountryName() {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.isValid("")
        );
        assertEquals(ERROR_INVALID_COUNTRY.formatted(""), exception.getMessage());
    }

    @Test
    @DisplayName("Should reject whitespace-only country name")
    void shouldRejectWhitespaceOnlyCountryName() {
        // When/Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validator.isValid("   ")
        );
        assertEquals(ERROR_INVALID_COUNTRY.formatted("   "), exception.getMessage());
    }
} 