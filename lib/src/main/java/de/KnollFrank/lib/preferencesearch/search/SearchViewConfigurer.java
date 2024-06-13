package de.KnollFrank.lib.preferencesearch.search;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.preference.Preference;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHosts;
import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;
import de.KnollFrank.lib.preferencesearch.SearchConfiguration;
import de.KnollFrank.lib.preferencesearch.common.PreferenceGroups;
import de.KnollFrank.lib.preferencesearch.results.SearchResultsPreferenceFragment;

class SearchViewConfigurer {

    public static void configureSearchView(final SearchView searchView,
                                           final SearchResultsPreferenceFragment searchResultsPreferenceFragment,
                                           final PreferenceSearcher preferenceSearcher,
                                           final SearchConfiguration searchConfiguration) {
        searchConfiguration.textHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(
                createOnQueryTextListener(
                        searchResultsPreferenceFragment,
                        preferenceSearcher));
    }

    private static OnQueryTextListener createOnQueryTextListener(
            final SearchResultsPreferenceFragment searchResultsPreferenceFragment,
            final PreferenceSearcher preferenceSearcher) {
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
                // FK-TODO: refactor && extract class
                setInvisible(preferenceSearcher.preferenceScreenWithHosts);
                final List<PreferenceWithHost> preferenceWithHostList = preferenceSearcher.searchFor(query);
                for (final PreferenceWithHost preferenceWithHost : preferenceWithHostList) {
                    setVisibleAndParents(preferenceWithHost.preference);
                }
            }

            private static void setInvisible(final PreferenceScreenWithHosts preferenceScreenWithHosts) {
                preferenceScreenWithHosts.preferenceScreen.setVisible(false);
                PreferenceGroups
                        .getAllChildren(preferenceScreenWithHosts.preferenceScreen)
                        .forEach(preference -> preference.setVisible(false));
            }

            private void setVisibleAndParents(final Preference preference) {
                if (preference != null) {
                    preference.setVisible(true);
                    setVisibleAndParents(preference.getParent());
                }
            }
        };
    }
}
