package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.Preference;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
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
            final Tree<PreferenceScreenWithHost, Preference, ImmutableValueGraph<PreferenceScreenWithHost, Preference>> preferenceScreenGraph,
            final Locale locale) {
        return TreeTransformerAlgorithm.transform(
                preferenceScreenGraph,
                createTreeTransformer(locale));
    }

    private TreeTransformer<PreferenceScreenWithHost, Preference, SearchablePreferenceScreenWithMap, SearchablePreference> createTreeTransformer(
            final Locale locale) {
        final PreferenceFragmentIdProvider preferenceFragmentIdProvider = createPreferenceFragmentUniqueLocalizedIdProvider(locale);
        return new TreeTransformer<>() {

            @Override
            public SearchablePreferenceScreenWithMap transformRootNode(final PreferenceScreenWithHost rootNode) {
                return convertToPojo(rootNode);
            }

            @Override
            public SearchablePreferenceScreenWithMap transformInnerNode(
                    final PreferenceScreenWithHost innerNode,
                    final ContextOfInnerNode<Preference, SearchablePreferenceScreenWithMap> contextOfInnerNode) {
                return convertToPojo(innerNode);
            }

            @Override
            public SearchablePreference transformEdgeValue(final Preference edgeValue,
                                                           final SearchablePreferenceScreenWithMap transformedParentNode) {
                return getTransformedPreference(edgeValue, transformedParentNode);
            }

            private SearchablePreferenceScreenWithMap convertToPojo(final PreferenceScreenWithHost node) {
                return preferenceScreenToSearchablePreferenceScreenConverter.convertPreferenceScreen(
                        node.preferenceScreen(),
                        node.host(),
                        preferenceFragmentIdProvider.getId(node.host()),
                        locale);
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

    private PreferenceFragmentLocalizedIdProvider createPreferenceFragmentUniqueLocalizedIdProvider(final Locale locale) {
        return new PreferenceFragmentLocalizedIdProvider(
                locale,
                new UniqueIdCheckingPreferenceFragmentIdProvider(preferenceFragmentIdProvider));
    }
}
