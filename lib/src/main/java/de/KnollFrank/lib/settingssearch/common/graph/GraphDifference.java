package de.KnollFrank.lib.settingssearch.common.graph;

import com.google.common.collect.Sets;

import org.jgrapht.Graph;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

/**
 * Performs a multi-level, detailed comparison of two SearchablePreferenceScreen graphs.
 * This class provides a deep-diff capability to precisely identify inconsistencies.
 */
public class GraphDifference {

    private final String differenceReport;
    private final boolean areEqual;

    private GraphDifference(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph1,
                            final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph2) {
        final StringBuilder reportBuilder = new StringBuilder();

        // Level 1: Structural comparison
        if (graph1.equals(graph2)) {
            // Level 2 & 3: Deep component-wise comparison
            this.areEqual =
                    compareScreenComponents(
                            reportBuilder,
                            graph1.vertexSet(),
                            graph2.vertexSet());
        } else {
            this.areEqual = false;
            buildStructuralDifferenceReport(graph1, graph2, reportBuilder);
        }

        this.differenceReport = reportBuilder.toString();
    }

    public static GraphDifference between(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph1,
                                          final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph2) {
        return new GraphDifference(graph1, graph2);
    }

    public boolean areEqual() {
        return areEqual;
    }

    @Override
    public String toString() {
        return areEqual ? "Graphs are equal." : differenceReport;
    }

    private void buildStructuralDifferenceReport(final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph1,
                                                 final Graph<SearchablePreferenceScreen, SearchablePreferenceEdge> graph2,
                                                 final StringBuilder sb) {
        sb.append("Graphs are structurally different (based on graph.equals()):\n");
        appendSetDifference(sb, "Screens", graph1.vertexSet(), graph2.vertexSet());
        appendSetDifference(sb, "Edges", graph1.edgeSet(), graph2.edgeSet());
    }

    private boolean compareScreenComponents(final StringBuilder sb,
                                            final Set<SearchablePreferenceScreen> screens1,
                                            final Set<SearchablePreferenceScreen> screens2) {
        boolean componentsAreEqual = true;
        for (final SearchablePreferenceScreen screen1 : screens1) {
            final SearchablePreferenceScreen screen2 =
                    screens2
                            .stream()
                            .filter(s -> s.equals(screen1))
                            .findFirst()
                            .orElseThrow(); // Should not happen

            // Compare simple components (host, title, summary)
            if (!screen1.host().equals(screen2.host())) {
                componentsAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: host is different.\n    Graph 1: %s\n    Graph 2: %s\n", screen1.id(), screen1.host(), screen2.host()));
            }
            if (!screen1.title().equals(screen2.title())) {
                componentsAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: title is different.\n    Graph 1: %s\n    Graph 2: %s\n", screen1.id(), screen1.title(), screen2.title()));
            }
            if (!screen1.summary().equals(screen2.summary())) {
                componentsAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: summary is different.\n    Graph 1: %s\n    Graph 2: %s\n", screen1.id(), screen1.summary(), screen2.summary()));
            }

            // Level 3: Deep compare 'allPreferencesOfPreferenceHierarchy'
            if (!compareContainedPreferences(screen1, screen2, sb)) {
                componentsAreEqual = false;
            }
        }
        if (!componentsAreEqual) {
            sb.insert(0, "Graphs are structurally equal, but their components differ:\n");
        }
        return componentsAreEqual;
    }

    private boolean compareContainedPreferences(final SearchablePreferenceScreen screen1,
                                                final SearchablePreferenceScreen screen2,
                                                final StringBuilder sb) {
        final Set<SearchablePreference> prefs1 = screen1.allPreferencesOfPreferenceHierarchy();
        final Set<SearchablePreference> prefs2 = screen2.allPreferencesOfPreferenceHierarchy();

        boolean preferencesAreEqual = true;

        // Check for preferences only in the first set, relying on the object's equals() method.
        final Set<SearchablePreference> prefsOnlyIn1 = Sets.difference(prefs1, prefs2);
        if (!prefsOnlyIn1.isEmpty()) {
            preferencesAreEqual = false;
            sb.append(String.format("  - Mismatch in Screen [id=%s]: 'allPreferencesOfPreferenceHierarchy' has items only in Graph 1 (cached):\n", screen1.id()));
            prefsOnlyIn1.forEach(pref -> sb.append(String.format("    - %s\n", pref)));
        }

        // Check for preferences only in the second set, relying on the object's equals() method.
        final Set<SearchablePreference> prefsOnlyIn2 = Sets.difference(prefs2, prefs1);
        if (!prefsOnlyIn2.isEmpty()) {
            preferencesAreEqual = false;
            sb.append(String.format("  - Mismatch in Screen [id=%s]: 'allPreferencesOfPreferenceHierarchy' has items only in Graph 2 (from DB):\n", screen1.id()));
            prefsOnlyIn2.forEach(pref -> sb.append(String.format("    - %s\n", pref)));
        }

        // For preferences considered equal by their equals() method, perform a deep state comparison via toString().
        final Set<SearchablePreference> commonPrefs = Sets.intersection(prefs1, prefs2);

        // To efficiently find the counterpart for comparison, create a temporary lookup map.
        final Map<SearchablePreference, SearchablePreference> prefs2Map =
                prefs2
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Function.identity(),
                                        Function.identity()));

        for (final SearchablePreference pref1 : commonPrefs) {
            final SearchablePreference pref2 = prefs2Map.get(pref1);
            if (pref2 == null) continue; // Should not happen

            final String pref1String = pref1.toString();
            final String pref2String = pref2.toString();

            if (!pref1String.equals(pref2String)) {
                preferencesAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: Contained Preference [id=%s] has different content:\n", screen1.id(), pref1.getId()));
                // ANPASSUNG HIER: Gib einfach beide Strings aus, anstatt den Unterschied zu markieren.
                sb.append(String.format("    Graph 1 (cached): %s\n", pref1String));
                sb.append(String.format("    Graph 2 (from DB):  %s\n", pref2String));
            }
        }

        return preferencesAreEqual;
    }

    private static <T> void appendSetDifference(final StringBuilder sb, final String setName, final Set<T> set1, final Set<T> set2) {
        final Set<T> onlyIn1 = Sets.difference(set1, set2);
        final Set<T> onlyIn2 = Sets.difference(set2, set1);

        if (!onlyIn1.isEmpty()) {
            sb.append(String.format("  %s only in Graph 1 (cached):\n", setName));
            onlyIn1.forEach(item -> sb.append(String.format("    - %s\n", item)));
        }
        if (!onlyIn2.isEmpty()) {
            sb.append(String.format("  %s only in Graph 2 (from DB):\n", setName));
            onlyIn2.forEach(item -> sb.append(String.format("    - %s\n", item)));
        }
    }
}
