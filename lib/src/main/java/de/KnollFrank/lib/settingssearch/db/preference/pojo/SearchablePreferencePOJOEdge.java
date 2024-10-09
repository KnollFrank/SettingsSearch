package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.graph.DefaultEdge;

public class SearchablePreferencePOJOEdge extends DefaultEdge {

    public final SearchablePreferencePOJO preference;

    public SearchablePreferencePOJOEdge(final SearchablePreferencePOJO preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return "(" + getSource() + " : " + getTarget() + " : " + preference + ")";
    }
}
