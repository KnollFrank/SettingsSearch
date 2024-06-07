package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.preference.Preference;

import com.bytehamster.lib.preferencesearch.common.UIUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// FK-TODO: rename
public class SearchPreferenceFragment2 extends Fragment {

    @IdRes
    private static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView2;

    @IdRes
    private int dummyFragmentContainerViewId = View.NO_ID;

    public SearchPreferenceFragment2() {
        super(R.layout.searchpreference_fragment2);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentContainerView fragmentContainerView = UIUtils.addFragmentContainerView2ViewGroup((ViewGroup) view, requireActivity());
        dummyFragmentContainerViewId = fragmentContainerView.getId();
        final PreferenceScreensProvider preferenceScreensProvider =
                new PreferenceScreensProvider(
                        new PreferenceFragments(requireActivity(), getChildFragmentManager(), dummyFragmentContainerViewId));
        // so erhält man Preferences startend mit den Preferences von PrefsFragment2, die wiederverwendet werden können.
        // FK-TODO: Eingaben im SearchView sollen eine Suche in den Preferences auslösen und die Suchergebnisse in PrefsFragmentFirst2 anzeigen. Eine ähnliche Methode schreiben wie in SearchPreferenceFragment.configureSearchView()
        final List<Preference> preferences =
                preferenceScreensProvider
                        .getPreferenceScreens(new PrefsFragment())
                        .stream()
                        .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                        .map(PreferenceProvider::getPreferences)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        if (savedInstanceState == null) {
            final SearchResultsPreferenceFragment searchResults = new SearchResultsPreferenceFragment();
            searchResults.setPreferences(preferences);
            Navigation.show(
                    searchResults,
                    false,
                    getChildFragmentManager(),
                    FRAGMENT_CONTAINER_VIEW);
        }
    }

    public static class PrefsFragment extends BaseSearchPreferenceFragment {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            addPreferencesFromResource(R.xml.preferences_multiple_screens2);
        }
    }
}
