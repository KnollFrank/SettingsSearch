package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public record GraphForLocale(
        Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
        Locale locale) {
}
