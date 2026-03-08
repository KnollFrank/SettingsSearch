package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.Functions;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

public record SearchablePreferenceScreenTree<C>(
        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
        Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree,
        Locale locale,
        C configuration) {

    public <D> SearchablePreferenceScreenTree<D> mapConfiguration(final Function<C, D> configurationMapper) {
        return new SearchablePreferenceScreenTree<>(
                tree,
                locale,
                configurationMapper.apply(configuration));
    }

    public <D> SearchablePreferenceScreenTree<D> asTreeHavingConfiguration(final D configuration) {
        return mapConfiguration(Functions.constant(configuration));
    }
}
