package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;
import java.util.StringJoiner;

public class SearchablePreferenceEntityEdge extends DefaultEdge {

    public final SearchablePreferenceEntity preference;

    public SearchablePreferenceEntityEdge(final SearchablePreferenceEntity preference) {
        this.preference = preference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceEntityEdge that = (SearchablePreferenceEntityEdge) o;
        return Objects.equals(getSource(), that.getSource()) && Objects.equals(getTarget(), that.getTarget()) && Objects.equals(preference, that.preference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget(), preference);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SearchablePreferenceEntityEdge.class.getSimpleName() + "[", "]")
                .add("source=" + getSource())
                .add("target=" + getTarget())
                .add("preference=" + preference)
                .toString();
    }
}
