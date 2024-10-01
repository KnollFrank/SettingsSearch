package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.IGraphTransformer;
import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;

class Preferences2SearchablePreferencesTransformer {

    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;

    public Preferences2SearchablePreferencesTransformer(final IsPreferenceSearchable isPreferenceSearchable,
                                                        final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
    }

    public Graph<SearchablePreferenceScreenWithHost, PreferenceEdge> transformPreferences2SearchablePreferences(
            final Graph<PreferenceScreenWithHost, PreferenceEdge> preferenceScreenGraph) {
        return GraphTransformer.transform(
                preferenceScreenGraph,
                PreferenceEdge.class,
                createGraphTransformer());
    }

    private IGraphTransformer<PreferenceScreenWithHost, PreferenceEdge, SearchablePreferenceScreenWithHost, PreferenceEdge> createGraphTransformer() {
        return new IGraphTransformer<>() {

            @Override
            public SearchablePreferenceScreenWithHost transformNode(final PreferenceScreenWithHost node) {
                return PreferenceScreenWithHostFactory.createSearchablePreferenceScreenWithHost(
                        node.host(),
                        isPreferenceSearchable,
                        searchableInfoAndDialogInfoProvider);
            }

            @Override
            public PreferenceEdge transformEdge(final PreferenceEdge edge, final SearchablePreferenceScreenWithHost transformedParentNode) {
                return new PreferenceEdge(getSearchablePreference(edge.preference, transformedParentNode));
            }

            private SearchablePreference getSearchablePreference(final Preference preference,
                                                                 final SearchablePreferenceScreenWithHost transformedParentNode) {
                return transformedParentNode
                        .searchablePreferenceScreen()
                        .searchablePreferenceByPreference()
                        .get(preference);
            }
        };
    }
}
