package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Locale;

public record SearchablePreferenceScreenGraph(
        Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
        Locale locale) {
}
