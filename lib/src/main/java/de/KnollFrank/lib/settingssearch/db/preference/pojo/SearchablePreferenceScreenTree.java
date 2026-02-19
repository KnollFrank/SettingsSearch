package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import com.google.common.graph.ImmutableValueGraph;

import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.common.Functions;
import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;

public record SearchablePreferenceScreenTree<C>(
        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
        Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree,
        LanguageCode languageCode,
        C configuration) {

    public <D> SearchablePreferenceScreenTree<D> mapConfiguration(final Function<C, D> configurationMapper) {
        return new SearchablePreferenceScreenTree<>(
                tree,
                languageCode,
                configurationMapper.apply(configuration));
    }

    public <D> SearchablePreferenceScreenTree<D> asTreeHavingConfiguration(final D configuration) {
        return mapConfiguration(Functions.constant(configuration));
    }
}
