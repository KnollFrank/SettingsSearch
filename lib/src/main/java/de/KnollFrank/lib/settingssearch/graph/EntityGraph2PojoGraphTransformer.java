package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class EntityGraph2PojoGraphTransformer {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> toPojoGraph(
            final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph) {
        return GraphTransformerAlgorithm.transform(
                entityGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer());
    }

    private static GraphTransformer<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge, SearchablePreferenceScreen, SearchablePreferenceEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public SearchablePreferenceScreen transformRootNode(final SearchablePreferenceScreenEntity rootNode) {
                return SearchablePreferenceScreenConverter.fromEntity(rootNode);
            }

            @Override
            public SearchablePreferenceScreen transformInnerNode(final SearchablePreferenceScreenEntity innerNode, final ContextOfInnerNode<SearchablePreferenceEntityEdge, SearchablePreferenceScreen> contextOfInnerNode) {
                return SearchablePreferenceScreenConverter.fromEntity(innerNode);
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final SearchablePreferenceEntityEdge edge, final SearchablePreferenceScreen transformedParentNode) {
                return new SearchablePreferenceEdge(SearchablePreferenceConverter.fromEntity(edge.preference));
            }
        };
    }
}
