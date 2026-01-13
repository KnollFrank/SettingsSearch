package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.graph.ValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.NodesTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class MapFromPojoNodesRemover {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static Tree<SearchablePreferenceScreen, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreen, SearchablePreference>> removeMapFromPojoNodes(
            final Tree<SearchablePreferenceScreenWithMap, SearchablePreference, ? extends ValueGraph<SearchablePreferenceScreenWithMap, SearchablePreference>> pojoGraph) {
        return NodesTransformer.transformNodes(
                pojoGraph,
                MapFromPojoNodesRemover::removeMapFromPojoNode);
    }

    public static SearchablePreferenceScreen removeMapFromPojoNode(final SearchablePreferenceScreenWithMap searchablePreferenceScreenWithMap) {
        return searchablePreferenceScreenWithMap.searchablePreferenceScreen();
    }
}
