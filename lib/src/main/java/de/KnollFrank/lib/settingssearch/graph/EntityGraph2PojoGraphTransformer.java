package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverterFactory.createScreenConverter;

import org.jgrapht.Graph;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;

// FK-TODO: diese Klasse einsetzen, siehe auch PojoGraph2EntityGraphTransformer
public class EntityGraph2PojoGraphTransformer {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> toPojoGraph(
            final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph,
            final DbDataProvider dbDataProvider) {
        return GraphTransformerAlgorithm.transform(
                entityGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer(createScreenConverter(dbDataProvider)));
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
                return new SearchablePreferenceEdge(getSearchablePreferenceById(transformedParentNode.allPreferences(), edge.preference.getId()));
            }

            private static SearchablePreference getSearchablePreferenceById(final Set<SearchablePreference> searchablePreferences,
                                                                            final int id) {
                return SearchablePreferences
                        .findPreferenceById(searchablePreferences, id)
                        .orElseThrow();
            }
        };
    }
}
