package de.KnollFrank.lib.settingssearch.search;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.Optional;

class SearchViewConfigurer {

    public static void configureSearchView(final SearchView searchView,
                                           final Optional<String> queryHint,
                                           final SearchAndDisplay searchAndDisplay) {
        queryHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(createOnQueryTextListener(searchAndDisplay));
    }

    private static OnQueryTextListener createOnQueryTextListener(final SearchAndDisplay searchAndDisplay) {
        return new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                searchAndDisplay.searchForQueryAndDisplayResults(newText);
                return true;
            }
        };
    }
}
