package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import org.jgrapht.Graph;

import java.util.Locale;

/*
FK-TODO:
1. a) für einige (z.B. häufig verwendete) Locales den SearchablePreferenceScreenGraph vorberechnen (und speichern)
   b) für andere Locales den SearchablePreferenceScreenGraph zur Laufzeit berechnen
- für jedes Plugin, welches eigene Preferences hat, seinen lokalisierten Teilgraph vorberechnen
- für jedes aktivierte Plugin und jedes Locale (=: Konfiguration) den Endgraphen aus den vorberechneten Graphen zur Laufzeit zusammenbauen (oder ebenfalls vorberechnen?).

NEXT-TODO: beginne damit, für das DE-Locale den SearchablePreferenceScreenGraph
      1. vorzuberechnen,
      2. zu speichern und dann
      3. zur Laufzeit zu verwenden.
*/
public record SearchablePreferenceScreenGraph(
        Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph,
        Locale locale) {
}
