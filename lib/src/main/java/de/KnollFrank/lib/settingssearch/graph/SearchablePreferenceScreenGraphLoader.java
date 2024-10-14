package de.KnollFrank.lib.settingssearch.graph;

import androidx.annotation.RawRes;
import androidx.preference.PreferenceManager;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.PreferenceEdge;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHostClass;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;

public class SearchablePreferenceScreenGraphLoader implements SearchablePreferenceScreenGraphProvider {

    private final @RawRes int searchablePreferenceScreenGraph;
    private final PreferenceManager preferenceManager;

    public SearchablePreferenceScreenGraphLoader(final @RawRes int searchablePreferenceScreenGraph,
                                                 final PreferenceManager preferenceManager) {
        this.searchablePreferenceScreenGraph = searchablePreferenceScreenGraph;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Graph<PreferenceScreenWithHostClass, PreferenceEdge> getSearchablePreferenceScreenGraph() {
        return SearchablePreferenceScreenGraphDAO.load(
                preferenceManager.getContext().getResources().openRawResource(searchablePreferenceScreenGraph),
                preferenceManager);
    }
}
