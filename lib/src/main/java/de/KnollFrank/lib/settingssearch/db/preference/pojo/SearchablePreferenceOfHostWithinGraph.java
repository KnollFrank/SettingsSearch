package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Objects;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferencePathProvider;

public record SearchablePreferenceOfHostWithinGraph(
        SearchablePreference searchablePreference,
        SearchablePreferenceScreen hostOfPreference,
        Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graphContainingPreference) {

    public PreferencePath getPreferencePath() {
        return PreferencePathProvider.getPreferencePath(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final SearchablePreferenceOfHostWithinGraph that = (SearchablePreferenceOfHostWithinGraph) o;
        return Objects.equals(searchablePreference, that.searchablePreference);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(searchablePreference);
    }
}
