package de.KnollFrank.settingssearch;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.graph.ComputeAndPersist;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphLoader;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphProvider;

public class SearchablePreferenceScreenGraphProviderWrapper {

    public enum GraphDAOMode {
        PERSIST_GRAPH, LOAD_GRAPH
    }

    public static SearchablePreferenceScreenGraphProvider wrapSearchablePreferenceScreenGraphProvider(
            final SearchablePreferenceScreenGraphProvider searchablePreferenceScreenGraphProvider,
            final GraphDAOMode graphDAOMode,
            final Context context) {
        return switch (graphDAOMode) {
            // FK-TODO: remove GraphDAOMode, just Compute the pojoGraph
            case PERSIST_GRAPH -> new ComputeAndPersist(
                    searchablePreferenceScreenGraphProvider,
                    context);
            case LOAD_GRAPH -> new SearchablePreferenceScreenGraphLoader(
                    R.raw.searchable_preference_screen_graph,
                    context);
        };
    }
}
