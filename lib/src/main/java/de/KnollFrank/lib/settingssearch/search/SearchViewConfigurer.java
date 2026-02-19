package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.LanguageCode;

class SearchViewConfigurer {

    private SearchViewConfigurer() {
    }

    public static <C> void configureSearchView(final SearchView searchView,
                                               final Optional<String> queryHint,
                                               final SearchAndDisplay<C> searchAndDisplay,
                                               final LanguageCode languageCode,
                                               final C actualConfiguration) {
        queryHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(createOnQueryTextListener(searchAndDisplay, languageCode, actualConfiguration));
    }

    private static <C> OnQueryTextListener createOnQueryTextListener(final SearchAndDisplay<C> searchAndDisplay,
                                                                     final LanguageCode languageCode,
                                                                     final C actualConfiguration) {
        return new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                searchAndDisplay.searchForQueryAndDisplayResults(newText, languageCode, actualConfiguration);
                return true;
            }
        };
    }
}
