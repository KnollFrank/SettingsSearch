package de.KnollFrank.lib.preferencesearch.search.matcher;

import java.util.Objects;

public class Match {

    public enum Type {
        TITLE, SUMMARY
    }

    public final Type type;
    public final int startInclusive;
    public final int endExclusive;

    public Match(final Type type, final int startInclusive, final int endExclusive) {
        this.type = type;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Match match = (Match) o;
        return startInclusive == match.startInclusive && endExclusive == match.endExclusive && type == match.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, startInclusive, endExclusive);
    }

    @Override
    public String toString() {
        return "Match{" +
                "type=" + type +
                ", startInclusive=" + startInclusive +
                ", endExclusive=" + endExclusive +
                '}';
    }
}
