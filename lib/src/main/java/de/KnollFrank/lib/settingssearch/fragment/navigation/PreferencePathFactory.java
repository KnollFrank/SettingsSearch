package de.KnollFrank.lib.settingssearch.fragment.navigation;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferences;
import de.KnollFrank.lib.settingssearch.graph.PojoGraphs;

class PreferencePathFactory {

    public static PreferencePath createPreferencePath(final PreferencePathData preferencePathData,
                                                      final SearchablePreferenceScreenGraphDAO graphDAO,
                                                      final Locale locale) {
        return createPreferencePath(
                preferencePathData,
                PojoGraphs.getPreferences(graphDAO.findGraphById(locale).orElseThrow().graph()));
    }

    private static PreferencePath createPreferencePath(final PreferencePathData preferencePathData,
                                                       final Set<SearchablePreference> haystack) {
        return new PreferencePath(
                asSearchablePreferences(
                        preferencePathData.preferenceIds(),
                        haystack));
    }

    private static List<SearchablePreference> asSearchablePreferences(final List<String> preferenceIds,
                                                                      final Set<SearchablePreference> haystack) {
        return preferenceIds
                .stream()
                .map(preferenceId -> asSearchablePreference(preferenceId, haystack))
                .collect(Collectors.toList());
    }

    private static SearchablePreference asSearchablePreference(final String preferenceId,
                                                               final Set<SearchablePreference> haystack) {
        return SearchablePreferences
                .findPreferenceById(haystack, preferenceId)
                .orElseThrow();
    }
}
