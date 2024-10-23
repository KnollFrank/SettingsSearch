package de.KnollFrank.lib.settingssearch.graph;

import android.content.Context;

import androidx.annotation.RawRes;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.db.preference.dao.POJOGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

public class SearchablePreferenceScreenGraphLoader implements SearchablePreferenceScreenGraphProvider {

    private final @RawRes int searchablePreferenceScreenGraph;
    private final Context context;

    public SearchablePreferenceScreenGraphLoader(final @RawRes int searchablePreferenceScreenGraph,
                                                 final Context context) {
        this.searchablePreferenceScreenGraph = searchablePreferenceScreenGraph;
        this.context = context;
    }

    @Override
    public Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> getSearchablePreferenceScreenGraph() {
        return POJOGraphDAO.load(
                context
                        .getResources()
                        .openRawResource(this.searchablePreferenceScreenGraph));
    }
}
