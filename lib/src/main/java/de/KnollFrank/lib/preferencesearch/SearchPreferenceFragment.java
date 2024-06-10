package de.KnollFrank.lib.preferencesearch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import java.util.List;

import de.KnollFrank.lib.preferencesearch.common.Keyboard;

public class SearchPreferenceFragment extends Fragment {

    private SearchConfiguration searchConfiguration;

    public static SearchPreferenceFragment newInstance(final SearchConfiguration searchConfiguration) {
        final SearchPreferenceFragment searchPreferenceFragment = new SearchPreferenceFragment();
        searchPreferenceFragment.setArguments(SearchConfigurations.toBundle(searchConfiguration));
        return searchPreferenceFragment;
    }

    public SearchPreferenceFragment() {
        super(R.layout.searchpreference_fragment);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchConfiguration = SearchConfigurations.fromBundle(requireArguments());
    }

    @Override
    public void onStart() {
        super.onStart();
        final View view = requireView();
        final FragmentContainerView dummyFragmentContainerView =
                UIUtils.createAndAddFragmentContainerView2ViewGroup(
                        (ViewGroup) view,
                        getContext());
        final List<PreferenceWithHost> preferenceWithHostList =
                this
                        .getPreferencesProvider(dummyFragmentContainerView.getId())
                        .getPreferences();
        final SearchResultsPreferenceFragment searchResultsPreferenceFragment =
                SearchResultsPreferenceFragment.newInstance(searchConfiguration.fragmentContainerViewId);
        {
            final SearchView searchView = view.findViewById(R.id.searchView);
            SearchViewConfigurer.configureSearchView(
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
                R.id.searchResultsFragmentContainerView);
    }

    private static class SearchViewConfigurer {

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
    }

    private void selectSearchView(final SearchView searchView) {
        searchView.requestFocus();
        Keyboard.showKeyboard(getActivity(), searchView);
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
