package de.KnollFrank.lib.settingssearch.graph;

import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableValueGraph;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;

// FK-TODO: refactor
// FK-TODO: stelle sicher, dass für alle Graphen actual und expected immer gilt: DotGraphDifference.between(actual, expected).areEqual() == GraphDifference.between(actual, expected).areEqual()
@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class GraphDifference {

    private final String differenceReport;
    private final boolean areEqual;

    private GraphDifference(final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> actual,
                            final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> expected) {
        final StringBuilder reportBuilder = new StringBuilder();

        // Level 1: Structural comparison
        if (actual.equals(expected)) {
            // Level 2 & 3: Deep component-wise comparison
            areEqual =
                    compareScreenComponents(
                            reportBuilder,
                            actual.nodes(),
                            expected.nodes());
        } else {
            areEqual = false;
            buildStructuralDifferenceReport(actual, expected, reportBuilder);
        }

        differenceReport = reportBuilder.toString();
    }

    public static GraphDifference between(final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> actual,
                                          final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> expected) {
        return new GraphDifference(actual, expected);
    }

    public boolean areEqual() {
        return areEqual;
    }

    @Override
    public String toString() {
        return areEqual ? "Graphs are equal." : differenceReport;
    }

    private void buildStructuralDifferenceReport(final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> actual,
                                                 final ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference> expected,
                                                 final StringBuilder sb) {
        sb.append("Graphs are structurally different (based on graph.equals()):\n");
        appendSetDifference(sb, "Screens", actual.nodes(), expected.nodes());
        appendSetDifference(sb, "Edges", actual.edges(), expected.edges());
    }

    private boolean compareScreenComponents(final StringBuilder sb,
                                            final Set<SearchablePreferenceScreen> actualScreens,
                                            final Set<SearchablePreferenceScreen> expectedScreens) {
        boolean componentsAreEqual = true;
        for (final SearchablePreferenceScreen actualScreen : actualScreens) {
            final SearchablePreferenceScreen expectedScreen =
                    SearchablePreferenceScreens
                            .findSearchablePreferenceScreenById(expectedScreens, actualScreen.id())
                            .orElseThrow();

            // Compare simple components (host, title, summary)
            if (!actualScreen.host().equals(expectedScreen.host())) {
                componentsAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: host is different.\n    actualScreen: %s\n    expectedScreen: %s\n", actualScreen.id(), actualScreen.host(), expectedScreen.host()));
            }
            if (!actualScreen.title().equals(expectedScreen.title())) {
                componentsAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: title is different.\n    actualScreen: %s\n    expectedScreen: %s\n", actualScreen.id(), actualScreen.title(), expectedScreen.title()));
            }
            if (!actualScreen.summary().equals(expectedScreen.summary())) {
                componentsAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: summary is different.\n    actualScreen: %s\n    expectedScreen: %s\n", actualScreen.id(), actualScreen.summary(), expectedScreen.summary()));
            }

            // Level 3: Deep compare 'allPreferencesOfPreferenceHierarchy'
            if (!compareContainedPreferences(actualScreen, expectedScreen, sb)) {
                componentsAreEqual = false;
            }
        }
        if (!componentsAreEqual) {
            sb.insert(0, "Graphs are structurally equal, but their components differ:\n");
        }
        return componentsAreEqual;
    }

    private boolean compareContainedPreferences(final SearchablePreferenceScreen actualScreen,
                                                final SearchablePreferenceScreen expectedScreen,
                                                final StringBuilder sb) {
        final Set<SearchablePreference> prefsActual = actualScreen.allPreferencesOfPreferenceHierarchy();
        final Set<SearchablePreference> prefsExpected = expectedScreen.allPreferencesOfPreferenceHierarchy();

        boolean preferencesAreEqual = true;

        // Check for preferences only in the first set, relying on the object's equals() method.
        final Set<SearchablePreference> prefsOnlyInActual = Sets.difference(prefsActual, prefsExpected);
        if (!prefsOnlyInActual.isEmpty()) {
            preferencesAreEqual = false;
            sb.append(String.format("  - Mismatch in Screen [id=%s]: 'allPreferencesOfPreferenceHierarchy' has items only in actual screen:\n", actualScreen.id()));
            prefsOnlyInActual.forEach(pref -> sb.append(String.format("    - %s\n", pref)));
        }

        // Check for preferences only in the second set, relying on the object's equals() method.
        final Set<SearchablePreference> prefsOnlyInExpected = Sets.difference(prefsExpected, prefsActual);
        if (!prefsOnlyInExpected.isEmpty()) {
            preferencesAreEqual = false;
            sb.append(String.format("  - Mismatch in Screen [id=%s]: 'allPreferencesOfPreferenceHierarchy' has items only in expected screen:\n", expectedScreen.id()));
            prefsOnlyInExpected.forEach(pref -> sb.append(String.format("    - %s\n", pref)));
        }

        // For preferences considered equal by their equals() method, perform a deep state comparison via toString().
        final Set<String> commonIds = Sets.intersection(getIds(prefsActual), getIds(prefsExpected));
        for (final String commonId : commonIds) {
            final String prefActualString =
                    SearchablePreferences
                            .findPreferenceById(prefsActual, commonId)
                            .orElseThrow()
                            .toString();
            final String prefExpectedString =
                    SearchablePreferences
                            .findPreferenceById(prefsExpected, commonId)
                            .orElseThrow()
                            .toString();
            if (!prefActualString.equals(prefExpectedString)) {
                preferencesAreEqual = false;
                sb.append(String.format("  - Mismatch in Screen [id=%s]: Contained Preference [id=%s] has different content:\n", actualScreen.id(), commonId));
                sb.append(String.format("    actual preference: %s\n", prefActualString));
                sb.append(String.format("    expected preference:  %s\n", prefExpectedString));
            }
        }
        return preferencesAreEqual;
    }

    private static Set<String> getIds(final Set<SearchablePreference> preferences) {
        return preferences
                .stream()
                .map(SearchablePreference::getId)
                .collect(Collectors.toSet());
    }

    private static <T> void appendSetDifference(final StringBuilder sb,
                                                final String setName,
                                                final Set<T> actual,
                                                final Set<T> expected) {
        final Set<T> onlyInActual = Sets.difference(actual, expected);
        final Set<T> onlyInExpected = Sets.difference(expected, actual);

        if (!onlyInActual.isEmpty()) {
            sb.append(String.format("  %s only in actual:\n", setName));
            onlyInActual.forEach(item -> sb.append(String.format("    - %s\n", item)));
        }
        if (!onlyInExpected.isEmpty()) {
            sb.append(String.format("  %s only in expected:\n", setName));
            onlyInExpected.forEach(item -> sb.append(String.format("    - %s\n", item)));
        }
    }
}
