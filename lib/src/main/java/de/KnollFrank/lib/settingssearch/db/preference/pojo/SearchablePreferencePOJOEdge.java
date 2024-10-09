package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;

public class SearchablePreferencePOJOEdge extends DefaultEdge {

    public final SearchablePreferencePOJO preference;

    public SearchablePreferencePOJOEdge(final SearchablePreferencePOJO preference) {
        this.preference = preference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferencePOJOEdge that = (SearchablePreferencePOJOEdge) o;
        return Objects.equals(getSource(), that.getSource()) && Objects.equals(getTarget(), that.getTarget()) && Objects.equals(preference, that.preference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget(), preference);
    }

    @Override
    public String toString() {
        return "(" + getSource() + " : " + getTarget() + " : " + preference + ")";
    }
}
