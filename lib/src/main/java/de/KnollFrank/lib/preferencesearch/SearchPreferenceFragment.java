package de.KnollFrank.lib.preferencesearch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import java.util.List;

public class SearchPreferenceFragment extends Fragment {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView2;

    private SearchConfiguration searchConfiguration;

    public SearchPreferenceFragment() {
        super(R.layout.searchpreference_fragment);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchConfiguration = SearchConfigurations.fromBundle(getArguments());
    }

    @Override
    public void onStart() {
        super.onStart();
        final View view = getView();
        final FragmentContainerView dummyFragmentContainerView =
                UIUtils.createAndAddFragmentContainerView2ViewGroup(
                        (ViewGroup) view,
                        getContext());
        final List<PreferenceWithHost> preferenceWithHostList =
                this
                        .getPreferencesProvider(dummyFragmentContainerView.getId())
                        .getPreferences();
        final SearchResultsPreferenceFragment searchResultsPreferenceFragment =
                new SearchResultsPreferenceFragment(searchConfiguration.fragmentContainerViewId);
        {
            final SearchView searchView = view.findViewById(R.id.searchView);
            configureSearchView(
                    searchView,
                    searchResultsPreferenceFragment,
                    new PreferenceSearcher<>(preferenceWithHostList),
                    searchConfiguration);
            selectSearchView(searchView);
        }
        Navigation.show(
                searchResultsPreferenceFragment,
                false,
                getChildFragmentManager(),
                FRAGMENT_CONTAINER_VIEW);
    }

    private static void configureSearchView(final SearchView searchView,
                                            final SearchResultsPreferenceFragment searchResultsPreferenceFragment,
                                            final PreferenceSearcher<PreferenceWithHost> preferenceSearcher,
                                            final SearchConfiguration searchConfiguration) {
        searchConfiguration.textHint.ifPresent(searchView::setQueryHint);
        searchView.setOnQueryTextListener(
                createOnQueryTextListener(
                        searchResultsPreferenceFragment,
                        preferenceSearcher));
    }

    private static OnQueryTextListener createOnQueryTextListener(
            final SearchResultsPreferenceFragment searchResultsPreferenceFragment,
            final PreferenceSearcher<PreferenceWithHost> preferenceSearcher) {
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
                searchResultsPreferenceFragment.setPreferenceWithHostList(
                        preferenceSearcher.searchFor(query));
            }
        };
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

    private IPreferencesProvider<PreferenceWithHost> getPreferencesProvider(final @IdRes int fragmentContainerViewId) {
        return new PreferencesProvider(
                searchConfiguration.rootPreferenceFragment.getName(),
                new PreferenceScreensProvider(
                        new PreferenceFragments(
                                requireActivity(),
                                getChildFragmentManager(),
                                fragmentContainerViewId)),
                getContext());
    }

    private static class UIUtils {

        public static FragmentContainerView createAndAddFragmentContainerView2ViewGroup(
                final ViewGroup viewGroup,
                final Context context) {
            final FragmentContainerView fragmentContainerView = createInvisibleFragmentContainerView(context);
            viewGroup.addView(fragmentContainerView);
            return fragmentContainerView;
        }

        private static FragmentContainerView createInvisibleFragmentContainerView(final Context context) {
            final FragmentContainerView fragmentContainerView = new FragmentContainerView(context);
            fragmentContainerView.setId(View.generateViewId());
            fragmentContainerView.setVisibility(View.INVISIBLE);
            return fragmentContainerView;
        }
    }
}
