package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import static com.bytehamster.lib.preferencesearch.PreferenceFragments.getPreferenceFragments;
import static com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample.FRAGMENT_CONTAINER_VIEW;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;
import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.bytehamster.preferencesearch.R;

public class PrefsFragmentFirst extends BaseSearchPreferenceFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        configureSearchPreference(findPreference("searchPreference"));
    }

    private void configureSearchPreference(final SearchPreference searchPreference) {
        final SearchConfiguration config = searchPreference.getSearchConfiguration();
        config.setActivity((AppCompatActivity) requireActivity());
        config.setFragmentContainerViewId(FRAGMENT_CONTAINER_VIEW);
        config.setPreferenceFragmentsSupplier(() ->
                getPreferenceFragments(
                        new PrefsFragmentFirst(),
                        getActivity(),
                        FRAGMENT_CONTAINER_VIEW));
        config.setBreadcrumbsEnabled(true);
        config.setHistoryEnabled(true);
        config.setFuzzySearchEnabled(false);
    }
}
