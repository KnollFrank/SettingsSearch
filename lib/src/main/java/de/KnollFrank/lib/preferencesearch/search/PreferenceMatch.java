package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;

import java.util.Objects;

class PreferenceMatch {

    public enum Type {
        TITLE, SUMMARY, ENTRY
    }

    public final Preference preference;
    public final Type type;
    public final IndexRange indexRange;

    public PreferenceMatch(final Preference preference,
                           final Type type,
                           final IndexRange indexRange) {
        this.preference = preference;
        this.type = type;
        this.indexRange = indexRange;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferenceMatch that = (PreferenceMatch) o;
        return Objects.equals(preference, that.preference) && type == that.type && Objects.equals(indexRange, that.indexRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preference, type, indexRange);
    }

    @Override
    public String toString() {
        return "PreferenceMatch{" +
                "preference=" + preference +
                ", type=" + type +
                ", indexRange=" + indexRange +
                '}';
    }
}
