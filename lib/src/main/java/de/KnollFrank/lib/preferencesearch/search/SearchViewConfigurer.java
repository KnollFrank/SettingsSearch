package de.KnollFrank.lib.preferencesearch.search;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.preference.Preference;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;
import de.KnollFrank.lib.preferencesearch.SearchConfiguration;

class SearchViewConfigurer {

    public static void configureSearchView(final SearchView searchView,
                                           final PreferenceSearcher preferenceSearcher,
                                           final SearchConfiguration searchConfiguration) {
        searchConfiguration.textHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(createOnQueryTextListener(preferenceSearcher));
    }

    private static OnQueryTextListener createOnQueryTextListener(final PreferenceSearcher preferenceSearcher) {
        return new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                filterPreferenceItemsBy(newText);
                return true;
            }

            private void filterPreferenceItemsBy(final String query) {
                final List<PreferenceWithHost> preferenceWithHostList = preferenceSearcher.searchFor(query);
                Preferences.makePreferencesOfPreferenceScreenVisible(
                        getPreferences(preferenceWithHostList),
                        preferenceSearcher.preferenceScreenWithHosts.preferenceScreen);
            }

            private static List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
                return preferenceWithHostList
                        .stream()
                        .map(preferenceWithHost -> preferenceWithHost.preference)
                        .collect(Collectors.toList());
            }
        };
    }
}
