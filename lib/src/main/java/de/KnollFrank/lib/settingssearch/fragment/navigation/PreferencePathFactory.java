package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.FragmentActivity;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenGraphRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinGraph;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraphs;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;

class PreferencePathFactory {

    public static <C> PreferencePath createPreferencePath(final PreferencePathData preferencePathData,
                                                          final SearchablePreferenceScreenGraphRepository<C> graphRepository,
                                                          final Locale locale,
                                                          final FragmentActivity activityContext) {
        return createPreferencePath(
                preferencePathData,
                PojoGraphs.getPreferences(
                        graphRepository
                                .findGraphById(locale, null, activityContext)
                                .orElseThrow()
                                .graph()));
    }

    private static PreferencePath createPreferencePath(final PreferencePathData preferencePathData,
                                                       final Set<SearchablePreferenceOfHostWithinGraph> haystack) {
        return new PreferencePath(
                asSearchablePreferences(
                        preferencePathData.preferenceIds(),
                        haystack));
    }

    private static List<SearchablePreferenceOfHostWithinGraph> asSearchablePreferences(
            final List<String> preferenceIds,
            final Set<SearchablePreferenceOfHostWithinGraph> haystack) {
        return preferenceIds
                .stream()
                .map(preferenceId -> asSearchablePreference(preferenceId, haystack))
                .collect(Collectors.toList());
    }


    private static SearchablePreferenceOfHostWithinGraph asSearchablePreference(
            final String id,
            final Set<SearchablePreferenceOfHostWithinGraph> haystack) {
        return SearchablePreferenceWithinGraphs
                .findPreferenceById(haystack, id)
                .orElseThrow();
    }
}
