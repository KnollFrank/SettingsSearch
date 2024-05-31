package com.bytehamster.preferencesearch;

import static com.bytehamster.lib.preferencesearch.PreferenceFragments.getPreferenceFragments;
import static com.bytehamster.preferencesearch.SearchViewExample.FRAGMENT_CONTAINER_VIEW;

import android.os.Bundle;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;
import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;

public class PrefsFragmentFirst extends BaseSearchPreferenceFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        configureSearchPreference(findPreference("searchPreference"));
    }

    private void configureSearchPreference(final SearchPreference searchPreference) {
        final SearchConfiguration searchConfiguration = searchPreference.getSearchConfiguration();
        searchConfiguration.setActivity(requireActivity());
        searchConfiguration.setFragmentContainerViewId(FRAGMENT_CONTAINER_VIEW);
        searchConfiguration.setPreferenceFragmentsSupplier(() ->
                getPreferenceFragments(
                        new PrefsFragmentFirst(),
                        getActivity(),
                        FRAGMENT_CONTAINER_VIEW));
        searchConfiguration.setBreadcrumbsEnabled(true);
        searchConfiguration.setHistoryEnabled(true);
        searchConfiguration.setFuzzySearchEnabled(false);
    }
}
