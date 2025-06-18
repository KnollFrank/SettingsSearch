package de.KnollFrank.lib.settingssearch.graph;

import org.jgrapht.Graph;

import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.GraphTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceEntityToSearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntityEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

// FK-TODO: add test analogous to PreferenceScreen2SearchablePreferenceScreenConverterTest
public class EntityGraph2PojoGraphTransformer {

    public static Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> toPojoGraph(
            final Graph<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge> entityGraph,
            final DbDataProviders dbDataProviders) {
        return GraphTransformerAlgorithm.transform(
                entityGraph,
                SearchablePreferenceEdge.class,
                createGraphTransformer(dbDataProviders));
    }

    private static GraphTransformer<SearchablePreferenceScreenEntity, SearchablePreferenceEntityEdge, SearchablePreferenceScreen, SearchablePreferenceEdge> createGraphTransformer(
            final DbDataProviders dbDataProviders) {
        return new GraphTransformer<>() {

            private final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter =
                    new SearchablePreferenceEntityToSearchablePreferenceConverter(
                            dbDataProviders.preferencedbDataProvider());
            private final SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter screenConverter =
                    new SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter(
                            entity -> entity.getAllPreferences(dbDataProviders.screenDbDataProvider()),
                            preferenceConverter);

            @Override
            public SearchablePreferenceScreen transformRootNode(final SearchablePreferenceScreenEntity rootNode) {
                return screenConverter.fromEntity(rootNode);
            }

            @Override
            public SearchablePreferenceScreen transformInnerNode(final SearchablePreferenceScreenEntity innerNode,
                                                                 final ContextOfInnerNode<SearchablePreferenceEntityEdge, SearchablePreferenceScreen> contextOfInnerNode) {
                // FK-FIXME: diese Converter "in Ordnung" bringen
                return screenConverter.fromEntity(innerNode);
            }

            @Override
            public SearchablePreferenceEdge transformEdge(final SearchablePreferenceEntityEdge edge,
                                                          final SearchablePreferenceScreen transformedParentNode) {
                return new SearchablePreferenceEdge(preferenceConverter.fromEntity(edge.preference));
            }
        };
    }
}
