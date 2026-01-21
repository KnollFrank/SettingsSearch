package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.GraphConverter;
import de.KnollFrank.lib.settingssearch.common.graph.ToGuavaGraphConverter;
import de.KnollFrank.lib.settingssearch.common.graph.ToJGraphTConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class GraphConverterFactory {

    private GraphConverterFactory() {
    }

    public static GraphConverter<SearchablePreferenceScreen, SearchablePreferenceEdge, SearchablePreference> createSearchablePreferenceScreenGraphConverter() {
        return new GraphConverter<>(
                new ToJGraphTConverter<>(
                        SearchablePreferenceEdge.class,
                        SearchablePreferenceEdge::new),
                new ToGuavaGraphConverter<>(searchablePreferenceEdge -> searchablePreferenceEdge.preference));
    }

    public static GraphConverter<PreferenceScreenWithHost, PreferenceEdge, Preference> createPreferenceScreenWithHostGraphConverter() {
        return new GraphConverter<>(
                new ToJGraphTConverter<>(
                        PreferenceEdge.class,
                        PreferenceEdge::new),
                new ToGuavaGraphConverter<>(preferenceEdge -> preferenceEdge.preference));
    }
}
