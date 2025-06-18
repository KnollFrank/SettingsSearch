package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverterFactory.createScreenConverter;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class EntityGraph2PojoGraphTransformer {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> toPojoGraph(
            final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph,
            final DbDataProviders dbDataProviders) {
        return GraphTransformerAlgorithm.transform(
                entityGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer(createScreenConverter(dbDataProviders)));
    }

    private static GraphTransformer<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge, SearchablePreferenceScreen, SearchablePreferenceEdge> createGraphTransformer(final SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter screenConverter) {
        return new GraphTransformer<>() {

            @Override
            public SearchablePreferenceScreen transformRootNode(final SearchablePreferenceScreenEntity rootNode) {
                return screenConverter.fromEntity(rootNode, Optional.empty());
            }

            @Override
            public SearchablePreferenceScreen transformInnerNode(final SearchablePreferenceScreenEntity innerNode,
                                                                 final ContextOfInnerNode<SearchablePreferenceEntityEdge, SearchablePreferenceScreen> contextOfInnerNode) {
                return screenConverter.fromEntity(innerNode, Optional.of(contextOfInnerNode.transformedParentNode()));
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final SearchablePreferenceEntityEdge edge,
                                                          final SearchablePreferenceScreen transformedParentNode) {
                return new SearchablePreferenceEdge(getSearchablePreferenceForEntity(edge.preference, transformedParentNode.allPreferences()));
            }

            private static SearchablePreference getSearchablePreferenceForEntity(final SearchablePreferenceEntity entity,
                                                                                 final Set<SearchablePreference> searchablePreferences) {
                return SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter
                        .getSearchablePreferenceById(searchablePreferences)
                        .get(entity.getId());
            }
        };
    }
}
