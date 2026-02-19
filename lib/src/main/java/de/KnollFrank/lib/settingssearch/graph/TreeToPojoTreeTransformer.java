package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformer;
import de.KnollFrank.lib.settingssearch.common.graph.TreeTransformerAlgorithm;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class TreeToPojoTreeTransformer {

    private final PreferenceScreenToSearchablePreferenceScreenConverter preferenceScreenToSearchablePreferenceScreenConverter;
    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public TreeToPojoTreeTransformer(final PreferenceScreenToSearchablePreferenceScreenConverter preferenceScreenToSearchablePreferenceScreenConverter,
                                     final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceScreenToSearchablePreferenceScreenConverter = preferenceScreenToSearchablePreferenceScreenConverter;
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public Tree<SearchablePreferenceScreenWithMap, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreenWithMap, SearchablePreference>> transformTreeToPojoTree(
            final Tree<PreferenceScreenOfHostOfActivity, Preference, ImmutableValueGraph<PreferenceScreenOfHostOfActivity, Preference>> preferenceScreenGraph,
            final LanguageCode languageCode) {
        return TreeTransformerAlgorithm.transform(
                preferenceScreenGraph,
                createTreeTransformer(languageCode));
    }

    private TreeTransformer<PreferenceScreenOfHostOfActivity, Preference, SearchablePreferenceScreenWithMap, SearchablePreference> createTreeTransformer(
            final LanguageCode languageCode) {
        final PreferenceFragmentIdProvider preferenceFragmentIdProvider = createPreferenceFragmentUniqueLocalizedIdProvider(languageCode);
        return new TreeTransformer<>() {

            @Override
            public SearchablePreferenceScreenWithMap transformRootNode(final PreferenceScreenOfHostOfActivity rootNode) {
                return convertToPojo(rootNode);
            }

            @Override
            public SearchablePreferenceScreenWithMap transformInnerNode(
                    final PreferenceScreenOfHostOfActivity innerNode,
                    final ContextOfInnerNode<Preference, SearchablePreferenceScreenWithMap> contextOfInnerNode) {
                return convertToPojo(innerNode);
            }

            @Override
            public SearchablePreference transformEdgeValue(final Preference edgeValue,
                                                           final SearchablePreferenceScreenWithMap transformedParentNode) {
                return getTransformedPreference(edgeValue, transformedParentNode);
            }

            private SearchablePreferenceScreenWithMap convertToPojo(final PreferenceScreenOfHostOfActivity node) {
                return preferenceScreenToSearchablePreferenceScreenConverter.convertPreferenceScreen(
                        node,
                        preferenceFragmentIdProvider.getId(node.hostOfPreferenceScreen()));
            }

            private static SearchablePreference getTransformedPreference(
                    final Preference preference,
                    final SearchablePreferenceScreenWithMap transformedParentNode) {
                return transformedParentNode
                        .pojoEntityMap()
                        .inverse()
                        .get(preference);
            }
        };
    }

    private PreferenceFragmentLocalizedIdProvider createPreferenceFragmentUniqueLocalizedIdProvider(final LanguageCode languageCode) {
        return new PreferenceFragmentLocalizedIdProvider(
                languageCode,
                new UniqueIdCheckingPreferenceFragmentIdProvider(preferenceFragmentIdProvider));
    }
}
