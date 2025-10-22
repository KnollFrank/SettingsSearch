package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import org.jgrapht.Graph;

import java.util.Locale;

/*
FK-TODO:
+ 1. a) für einige (z.B. häufig verwendete) Locales den SearchablePreferenceScreenGraph vorberechnen (und speichern)
     b) für andere Locales den SearchablePreferenceScreenGraph zur Laufzeit berechnen
- 2. für jedes Plugin, welches eigene Preferences hat, seinen lokalisierten Teilgraph zur Laufzeit berechnen und in den SearchablePreferenceScreenGraph einhängen, analog zu PrefsFragmentFirst.subtreeReplacer.replaceSubtreeWithTree()
- 3. teste in der Navigationsapp
     + 1. a) und
     - 1. b)
*/
public record SearchablePreferenceScreenGraph(
        Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
        Locale locale,
        PersistableBundle configuration,
        // FK-TODO: remove processed
        boolean processed) {

    public SearchablePreferenceScreenGraph asProcessedGraph() {
        return new SearchablePreferenceScreenGraph(graph, locale, configuration, true);
    }

    public SearchablePreferenceScreenGraph asGraphHavingConfiguration(final PersistableBundle configuration) {
        return new SearchablePreferenceScreenGraph(graph, locale, configuration, processed);
    }
}
