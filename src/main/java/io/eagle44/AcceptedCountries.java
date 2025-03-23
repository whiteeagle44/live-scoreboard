package io.eagle44;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class AcceptedCountries {
    private static final Set<String> COUNTRIES = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    static {
        COUNTRIES.addAll(List.of(
            "Argentina", "Australia", "Austria", "Belgium", "Brazil", "Canada", "China", "Colombia", 
            "Croatia", "Czech Republic", "Denmark", "Egypt", "England", "Finland", "France", "Germany", 
            "Greece", "Hungary", "India", "Indonesia", "Ireland", "Israel", "Italy", "Japan", "Korea", 
            "Malaysia", "Mexico", "Netherlands", "New Zealand", "Nigeria", "Norway", "Philippines", 
            "Poland", "Portugal", "Romania", "Russia", "Saudi Arabia", "Singapore", "South Africa", 
            "Spain", "Sweden", "Switzerland", "Taiwan", "Thailand", "Turkey", "Ukraine", "United Arab Emirates", 
            "United States", "Vietnam"
        ));
    }

    private AcceptedCountries() {
        // Prevent instantiation
    }

    public static List<String> getAllCountries() {
        return List.copyOf(COUNTRIES);
    }

    public static boolean contains(String countryName) {
        return COUNTRIES.contains(countryName);
    }

    public static int size() {
        return COUNTRIES.size();
    }
} 