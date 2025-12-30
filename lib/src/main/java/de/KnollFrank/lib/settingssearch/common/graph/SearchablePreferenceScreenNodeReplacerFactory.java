package de.KnollFrank.lib.settingssearch.common.graph;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceScreenNodeReplacerFactory {

    public static NodeReplacer<SearchablePreferenceScreen, SearchablePreferenceEdge> createNodeReplacer() {
        return new NodeReplacer<>(
                SearchablePreferenceEdge.class,
                edge -> new SearchablePreferenceEdge(edge.preference));
    }
}
