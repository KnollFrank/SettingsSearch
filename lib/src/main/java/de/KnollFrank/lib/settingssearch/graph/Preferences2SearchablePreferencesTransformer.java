package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;

public class Preferences2SearchablePreferencesTransformer {

    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;

    public Preferences2SearchablePreferencesTransformer(final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
    }

    public Graph<PreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformerAlgorithm.transform(
                preferenceScreenGraph,
                PreferenceEdge.class,
                createGraphTransformer());
    }

    private GraphTransformer<PreferenceScreenWithHost, PreferenceEdge, PreferenceScreenWithHost, PreferenceEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            private final Map<PreferenceScreen, Map<Preference, SearchablePreference>> searchablePreferenceScreenWithMap = new HashMap<>();

            @Override
            public PreferenceScreenWithHost transformNode(final PreferenceScreenWithHost preferenceScreenWithHost) {
                final SearchablePreferenceTransformer transformer =
                        new SearchablePreferenceTransformer(
                                preferenceScreenWithHost.host().getPreferenceManager(),
                                preferenceScreenWithHost.host(),
                                searchableInfoAndDialogInfoProvider);
                final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap =
                        transformer.transform2SearchablePreferenceScreen(preferenceScreenWithHost.host().getPreferenceScreen());
                rememberSearchablePreferenceScreenWithMap(searchablePreferenceScreenWithMap);
                return new PreferenceScreenWithHost(
                        searchablePreferenceScreenWithMap.searchablePreferenceScreen(),
                        preferenceScreenWithHost.host());
            }

            @Override
            public PreferenceEdge transformEdge(final PreferenceEdge edge, final PreferenceScreenWithHost transformedParentNode) {
                return new PreferenceEdge(getSearchablePreference(edge.preference, transformedParentNode));
            }

            private SearchablePreference getSearchablePreference(final Preference preference,
                                                                 final PreferenceScreenWithHost transformedParentNode) {
                return restoreSearchablePreferenceScreenWithMap(transformedParentNode).searchablePreferenceByPreference().get(preference);
            }

            private void rememberSearchablePreferenceScreenWithMap(final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap) {
                this.searchablePreferenceScreenWithMap.put(
                        searchablePreferenceScreenWithMap.searchablePreferenceScreen(),
                        searchablePreferenceScreenWithMap.searchablePreferenceByPreference());
            }

            private SearchablePreferenceScreenWithMap restoreSearchablePreferenceScreenWithMap(final PreferenceScreenWithHost transformedParentNode) {
                return new SearchablePreferenceScreenWithMap(
                        transformedParentNode.preferenceScreen(),
                        searchablePreferenceScreenWithMap.get(transformedParentNode.preferenceScreen()));
            }
        };
    }
}
