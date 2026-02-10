package de.KnollFrank.lib.settingssearch.fragment.navigation;

import androidx.fragment.app.FragmentActivity;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.db.SearchablePreferenceScreenTreeRepository;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinTrees;
import de.KnollFrank.lib.settingssearch.graph.PojoTrees;

class PreferencePathFactory {

    private PreferencePathFactory() {
    }

    public static <C> PreferencePath createPreferencePath(final PreferencePathData preferencePathData,
                                                          final SearchablePreferenceScreenTreeRepository<C> treeRepository,
                                                          final Locale locale,
                                                          final C actualConfiguration,
                                                          final FragmentActivity activityContext) {
        return createPreferencePath(
                preferencePathData,
                PojoTrees.getPreferences(
                        treeRepository
                                .findTreeById(locale, actualConfiguration, activityContext)
                                .orElseThrow()
                                .tree()));
    }

    private static PreferencePath createPreferencePath(final PreferencePathData preferencePathData,
                                                       final Set<SearchablePreferenceOfHostWithinTree> haystack) {
        return new PreferencePath(
                asSearchablePreferences(
                        preferencePathData.preferenceIds(),
                        haystack));
    }

    private static List<SearchablePreferenceOfHostWithinTree> asSearchablePreferences(
            final List<String> preferenceIds,
            final Set<SearchablePreferenceOfHostWithinTree> haystack) {
        return preferenceIds
                .stream()
                .map(preferenceId -> asSearchablePreference(preferenceId, haystack))
                .collect(Collectors.toList());
    }


    private static SearchablePreferenceOfHostWithinTree asSearchablePreference(
            final String id,
            final Set<SearchablePreferenceOfHostWithinTree> haystack) {
        return SearchablePreferenceWithinTrees
                .findPreferenceById(haystack, id)
                .orElseThrow();
    }
}
