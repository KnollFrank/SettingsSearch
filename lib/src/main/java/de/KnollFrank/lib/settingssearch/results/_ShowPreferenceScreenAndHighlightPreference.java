package de.KnollFrank.lib.settingssearch.results;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

// FK-TODO: remove
class _ShowPreferenceScreenAndHighlightPreference {

    private final SearchResultsDescription searchResultsDescription;
    private final ShowPreferenceScreenAndHighlightPreference showPreferenceScreenAndHighlightPreference;

    public _ShowPreferenceScreenAndHighlightPreference(
            final PreferencePathNavigator preferencePathNavigator,
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final @IdRes int fragmentContainerViewId,
            final SearchResultsDescription searchResultsDescription,
            final PrepareShow prepareShow,
            final FragmentManager fragmentManager) {
        this.searchResultsDescription = searchResultsDescription;
        this.showPreferenceScreenAndHighlightPreference =
                new ShowPreferenceScreenAndHighlightPreference(
                        preferencePathNavigator,
                        preferencePathByPreference,
                        fragmentContainerViewId,
                        prepareShow,
                        fragmentManager);
    }

    public void showPreferenceScreenAndHighlightPreference(final Preference preference) {
        showPreferenceScreenAndHighlightPreference.showPreferenceScreenAndHighlightPreference(getSearchablePreferencePOJO(preference));
    }

    private SearchablePreferencePOJO getSearchablePreferencePOJO(final Preference preference) {
        return searchResultsDescription
                .preferenceScreenWithMap()
                .pojoEntityMap()
                .inverse()
                .get(preference);
    }
}
