package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import com.google.common.graph.ImmutableValueGraph;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.common.graph.Tree;

/*
FK-TODO:
+ 1. a) für einige (z.B. häufig verwendete) Locales den SearchablePreferenceScreenGraph vorberechnen (und speichern)
     b) für andere Locales den SearchablePreferenceScreenGraph zur Laufzeit berechnen
- 2. für jedes Plugin, welches eigene Preferences hat, seinen lokalisierten Teilgraph zur Laufzeit berechnen und in den SearchablePreferenceScreenGraph einhängen, analog zu PrefsFragmentFirst.subtreeReplacer.replaceSubtreeWithTree()
- 3. teste in der Navigationsapp
     + 1. a) und
     - 1. b)
*/
public record SearchablePreferenceScreenTree(
        @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
        Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> tree,
        Locale locale,
        PersistableBundle configuration) {

    public SearchablePreferenceScreenTree asGraphHavingConfiguration(final PersistableBundle configuration) {
        return new SearchablePreferenceScreenTree(tree, locale, configuration);
    }
}
