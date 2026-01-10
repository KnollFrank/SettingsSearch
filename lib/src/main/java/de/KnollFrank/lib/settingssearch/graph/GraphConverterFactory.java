package de.KnollFrank.lib.settingssearch.graph;

import de.KnollFrank.lib.settingssearch.common.graph.GraphConverter;
import de.KnollFrank.lib.settingssearch.common.graph.ToGuavaGraphConverter;
import de.KnollFrank.lib.settingssearch.common.graph.ToJGraphTConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphConverterFactory {

    public static GraphConverter<SearchablePreferenceScreen, SearchablePreferenceEdge, SearchablePreference> createGraphConverter() {
        return new GraphConverter<>(
                new ToJGraphTConverter<>(
                        SearchablePreferenceEdge.class,
                        SearchablePreferenceEdge::new),
                new ToGuavaGraphConverter<>(searchablePreferenceEdge -> searchablePreferenceEdge.preference));
    }
}
