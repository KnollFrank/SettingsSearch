package de.KnollFrank.lib.settingssearch.graph;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverterFactory.createScreenConverter;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;

public class EntityTreeToPojoTreeTransformer {

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public static Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> toPojoTree(
            final Tree<SearchablePreferenceScreenEntity, SearchablePreferenceEntity, ImmutableValueGraph<SearchablePreferenceScreenEntity, SearchablePreferenceEntity>> entityTree,
            final DbDataProvider dbDataProvider) {
        return TreeTransformerAlgorithm.transform(
                entityTree,
                createTreeTransformer(createScreenConverter(dbDataProvider)));
    }

    private static TreeTransformer<SearchablePreferenceScreenEntity, SearchablePreferenceEntity, SearchablePreferenceScreen, SearchablePreference> createTreeTransformer(final SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter screenConverter) {
        return new TreeTransformer<>() {

            @Override
            public SearchablePreferenceScreen transformRootNode(final SearchablePreferenceScreenEntity rootNode) {
                return screenConverter.fromEntity(rootNode);
            }

            @Override
            public SearchablePreferenceScreen transformInnerNode(final SearchablePreferenceScreenEntity innerNode,
                                                                 final ContextOfInnerNode<SearchablePreferenceEntity, SearchablePreferenceScreen> contextOfInnerNode) {
                return screenConverter.fromEntity(innerNode);
            }

            @Override
            public SearchablePreference transformEdgeValue(final SearchablePreferenceEntity edgeValue,
                                                           final SearchablePreferenceScreen transformedParentNode) {
                return SearchablePreferences
                        .findPreferenceById(
                                transformedParentNode.allPreferencesOfPreferenceHierarchy(),
                                edgeValue.id())
                        .orElseThrow();
            }
        };
    }
}
