package de.KnollFrank.lib.preferencesearch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.UIUtils;

// FK-TODO: rename
public class SearchPreferenceFragment2 extends Fragment {

    @IdRes
    private static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView2;

    private SearchConfiguration searchConfiguration;
    private final SearchResultsPreferenceFragment searchResults = new SearchResultsPreferenceFragment();

    public SearchPreferenceFragment2() {
        super(R.layout.searchpreference_fragment2);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchConfiguration = SearchConfigurations.fromBundle(getArguments());
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        @IdRes final int dummyFragmentContainerViewId =
                UIUtils
                        .createAndAddFragmentContainerView2ViewGroup((ViewGroup) view, getContext())
                        .getId();
        final List<PreferenceWrapper> preferences =
                this
                        .getPreferencesProvider(dummyFragmentContainerViewId)
                        .getPreferences();
        {
            final SearchView searchView = view.findViewById(R.id.searchView);
            configureSearchView(
                    searchView,
                    searchResults,
                    new PreferenceSearcher<>(preferences),
                    searchConfiguration);
            selectSearchView(searchView);
        }
        if (savedInstanceState == null) {
            searchResults.setPreferences(asPreferences(preferences));
            // FK-FIXME: when reaching SearchPreferenceFragment2 via the back button then changed preferences don't show their changed values (e.g. checkboxes)
            Navigation.show(
                    searchResults,
                    false,
                    getChildFragmentManager(),
                    FRAGMENT_CONTAINER_VIEW);
        }
    }

    private static void configureSearchView(final SearchView searchView,
                                            final SearchResultsPreferenceFragment searchResults,
                                            final PreferenceSearcher<PreferenceWrapper> preferenceSearcher,
                                            final SearchConfiguration searchConfiguration) {
        if (searchConfiguration.getTextHint() != null) {
            searchView.setQueryHint(searchConfiguration.getTextHint());
        }
        searchView.setOnQueryTextListener(
                createOnQueryTextListener(
                        searchResults,
                        preferenceSearcher));
    }

    private static OnQueryTextListener createOnQueryTextListener(
            final SearchResultsPreferenceFragment searchResults,
            final PreferenceSearcher<PreferenceWrapper> preferenceSearcher) {
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
                searchResults.setPreferences(asPreferences(preferenceSearcher.searchFor(query)));
            }
        };
    }

    // FK-TODO: DRY with SearchPreferenceFragment
    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        showKeyboard(searchView);
    }

    // FK-TODO: DRY with SearchPreferenceFragment
    private void showKeyboard(final View view) {
        final InputMethodManager inputMethodManager = getInputMethodManager();
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // FK-TODO: DRY with SearchPreferenceFragment
    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private static List<Preference> asPreferences(final List<PreferenceWrapper> preferenceWrappers) {
        return preferenceWrappers
                .stream()
                .map(preferenceWrapper -> preferenceWrapper.preference)
                .collect(Collectors.toList());
    }

    private IPreferencesProvider<PreferenceWrapper> getPreferencesProvider(final int fragmentContainerViewId) {
        return new PreferencesProvider(
                searchConfiguration.getRootPreferenceFragment().getName(),
                new PreferenceScreensProvider(
                        new PreferenceFragments(
                                requireActivity(),
                                getChildFragmentManager(),
                                fragmentContainerViewId)),
                getContext());
    }
}
