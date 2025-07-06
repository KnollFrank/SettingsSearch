package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.Locale;
import java.util.Optional;

class SearchViewConfigurer {

    public static void configureSearchView(final SearchView searchView,
                                           final Optional<String> queryHint,
                                           final SearchAndDisplay searchAndDisplay,
                                           final Locale locale) {
        queryHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(createOnQueryTextListener(searchAndDisplay, locale));
    }

    private static OnQueryTextListener createOnQueryTextListener(final SearchAndDisplay searchAndDisplay,
                                                                 final Locale locale) {
        return new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                searchAndDisplay.searchForQueryAndDisplayResults(newText, locale);
                return true;
            }
        };
    }
}
