package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceToSearchablePreferenceEntityConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class PojoGraph2EntityGraphTransformer {

    public static Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> toEntityGraph(
            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> pojoGraph) {
        return GraphTransformerAlgorithm.transform(
                pojoGraph,
                SearchablePreferenceEntityEdge.class,
                createGraphTransformer());
    }

    private static GraphTransformer<SearchablePreferenceScreen, SearchablePreferenceEdge, SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> createGraphTransformer() {
        return new GraphTransformer<>() {

            @Override
            public SearchablePreferenceScreenEntity transformRootNode(final SearchablePreferenceScreen rootNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(rootNode);
            }

            @Override
            public SearchablePreferenceScreenEntity transformInnerNode(final SearchablePreferenceScreen innerNode, final ContextOfInnerNode<SearchablePreferenceEdge, SearchablePreferenceScreenEntity> contextOfInnerNode) {
                return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter.toEntity(innerNode);
            }

            @Override
            public SearchablePreferenceEntityEdge transformEdge(final SearchablePreferenceEdge edge, final SearchablePreferenceScreenEntity transformedParentNode) {
                return new SearchablePreferenceEntityEdge(
                        SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                edge.preference,
                                // FK-FIXME: not correct: Optional.empty()
                                Optional.empty(),
                                transformedParentNode.getId()));
            }
        };
    }
}
