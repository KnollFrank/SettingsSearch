package de.KnollFrank.lib.preferencesearch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.Lists;

public class SearchPreferenceFragment extends Fragment implements SearchClickListener {

    public static final String TAG = "SearchPreferenceFragment";
    private List<PreferenceItem> preferenceItems;
    private SearchConfiguration searchConfiguration;

    public SearchPreferenceFragment() {
        super(R.layout.searchpreference_fragment);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchConfiguration = SearchConfigurations.fromBundle(getArguments());
        // FK-TODO: preferenceItems über einen IPreferencesProvider<PreferenceItem> auslesen
        preferenceItems = PreferenceItemsBundle.readPreferenceItems(getArguments());
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SearchPreferenceAdapter searchPreferenceAdapter =
                createAndConfigureSearchPreferenceAdapter(searchConfiguration, this);
        configureRecyclerView(view.findViewById(R.id.list), searchPreferenceAdapter);
        {
            final SearchView searchView = view.findViewById(R.id.searchView);
            configureSearchView(
                    searchView,
                    searchPreferenceAdapter,
                    new PreferenceSearcher<>(preferenceItems),
                    searchConfiguration);
            selectSearchView(searchView);
        }
    }

    @Override
    public void onItemClicked(final PreferenceItem preferenceItem, final int position) {
        ((SearchPreferenceResultListener) getActivity()).onSearchResultClicked(getSearchPreferenceResult(preferenceItem));
    }

    private static SearchPreferenceResult getSearchPreferenceResult(final PreferenceItem preferenceItem) {
        return new SearchPreferenceResult(
                preferenceItem.key.orElse(null),
                preferenceItem.resId,
                Lists
                        .getLastElement(preferenceItem.keyBreadcrumbs)
                        .orElse(null));
    }

    private static SearchPreferenceAdapter createAndConfigureSearchPreferenceAdapter(
            final SearchConfiguration searchConfiguration,
            final SearchClickListener onItemClickListener) {
        final SearchPreferenceAdapter searchPreferenceAdapter = new SearchPreferenceAdapter();
        searchPreferenceAdapter.setSearchConfiguration(searchConfiguration);
        searchPreferenceAdapter.setOnItemClickListener(onItemClickListener);
        return searchPreferenceAdapter;
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        showKeyboard(searchView);
    }

    private void showKeyboard(final View view) {
        final InputMethodManager inputMethodManager = getInputMethodManager();
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void configureRecyclerView(final RecyclerView recyclerView,
                                       final SearchPreferenceAdapter searchPreferenceAdapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchPreferenceAdapter);
    }

    private static void configureSearchView(final SearchView searchView,
                                            final SearchPreferenceAdapter searchPreferenceAdapter,
                                            final PreferenceSearcher<PreferenceItem> preferenceSearcher,
                                            final SearchConfiguration searchConfiguration) {
        if (searchConfiguration.getTextHint() != null) {
            searchView.setQueryHint(searchConfiguration.getTextHint());
        }
        searchView.setOnQueryTextListener(
                createOnQueryTextListener(
                        searchPreferenceAdapter,
                        preferenceSearcher));
    }

    private static OnQueryTextListener createOnQueryTextListener(
            final SearchPreferenceAdapter searchPreferenceAdapter,
            final PreferenceSearcher<PreferenceItem> preferenceSearcher) {
        return new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                filterPreferenceItemsBy(newText);
                return true;
            }

            private void filterPreferenceItemsBy(final String query) {
                searchPreferenceAdapter.setPreferenceItems(
                        ImmutableList.copyOf(
                                preferenceSearcher.searchFor(query)));
            }
        };
    }
}
