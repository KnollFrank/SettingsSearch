package de.KnollFrank.lib.preferencesearch.search;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;
import de.KnollFrank.lib.preferencesearch.SearchConfiguration;

class SearchViewConfigurer {

    public static void configureSearchView(final SearchView searchView,
                                           final PreferenceScreen preferenceScreen,
                                           final PreferenceSearcher preferenceSearcher,
                                           final SearchConfiguration searchConfiguration) {
        searchConfiguration.textHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(createOnQueryTextListener(preferenceSearcher, preferenceScreen));
    }

    private static OnQueryTextListener createOnQueryTextListener(
            final PreferenceSearcher preferenceSearcher,
            final PreferenceScreen preferenceScreen) {
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
                // FK-TODO: den gefundenen Suchtext query in den Suchergebnissen farblich hervorheben
                Preferences.makePreferencesOfPreferenceScreenVisible(
                        getPreferences(preferenceWithHostList),
                        preferenceScreen);
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
