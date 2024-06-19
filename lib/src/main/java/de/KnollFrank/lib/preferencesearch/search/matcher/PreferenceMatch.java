package de.KnollFrank.lib.preferencesearch.search.matcher;

import androidx.preference.Preference;

import java.util.Objects;

public class PreferenceMatch {

    public enum Type {
        TITLE, SUMMARY
    }

    public final Preference preference;
    public final Type type;
    public final int startInclusive;
    public final int endExclusive;

    public PreferenceMatch(final Preference preference,
                           final Type type,
                           // FK-TODO: combine startInclusive and endExclusive into a new Interval class
                           final int startInclusive,
                           final int endExclusive) {
        this.preference = preference;
        this.type = type;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PreferenceMatch preferenceMatch = (PreferenceMatch) o;
        return startInclusive == preferenceMatch.startInclusive && endExclusive == preferenceMatch.endExclusive && Objects.equals(preference, preferenceMatch.preference) && type == preferenceMatch.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(preference, type, startInclusive, endExclusive);
    }

    @Override
    public String toString() {
        return "PreferenceMatch{" +
                "preference=" + preference +
                ", type=" + type +
                ", startInclusive=" + startInclusive +
                ", endExclusive=" + endExclusive +
                '}';
    }
}
