package de.KnollFrank.lib.preferencesearch;

import androidx.preference.Preference;

import java.util.List;
import java.util.Objects;

public class PreferencePath {

    public final List<Preference> preferences;

    public PreferencePath(final List<Preference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferencePath that = (PreferencePath) o;
        return Objects.equals(preferences, that.preferences);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(preferences);
    }

    @Override
    public String toString() {
        return "PreferencePath{" +
                "preferences=" + preferences +
                '}';
    }
}
