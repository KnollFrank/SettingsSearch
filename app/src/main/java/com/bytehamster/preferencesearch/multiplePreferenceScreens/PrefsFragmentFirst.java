package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.bytehamster.preferencesearch.R;

public class PrefsFragmentFirst extends BaseFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        configureSearchPreference(findPreference("searchPreference"));
    }

    private void configureSearchPreference(final SearchPreference searchPreference) {
        final SearchConfiguration config = searchPreference.getSearchConfiguration();
        config.setActivity((AppCompatActivity) getActivity());
        config.setFragmentContainerViewId(android.R.id.content);

        // FK-TODO: PreferencesGraphProvider und preferencesGraph verwenden
        config.index(R.xml.preferences_multiple_screens).addBreadcrumb("Main file");
        config.index(R.xml.preferences2).addBreadcrumb("Second file");
        config.index(R.xml.preferences3).addBreadcrumb("Third file");
        config.setBreadcrumbsEnabled(true);
        config.setHistoryEnabled(true);
        config.setFuzzySearchEnabled(true);
    }
}
