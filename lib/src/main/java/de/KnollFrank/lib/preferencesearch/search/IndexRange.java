package de.KnollFrank.lib.preferencesearch.search;

import java.util.Objects;

class IndexRange {

    public final int startIndexInclusive;
    public final int endIndexExclusive;

    public IndexRange(final int startIndexInclusive, final int endIndexExclusive) {
        this.startIndexInclusive = startIndexInclusive;
        this.endIndexExclusive = endIndexExclusive;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final IndexRange that = (IndexRange) o;
        return startIndexInclusive == that.startIndexInclusive && endIndexExclusive == that.endIndexExclusive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndexInclusive, endIndexExclusive);
    }

    @Override
    public String toString() {
        return "IndexRange{" +
                "startIndexInclusive=" + startIndexInclusive +
                ", endIndexExclusive=" + endIndexExclusive +
                '}';
    }
}
