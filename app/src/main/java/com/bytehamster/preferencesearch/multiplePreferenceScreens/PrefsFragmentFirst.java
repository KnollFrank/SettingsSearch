package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.PreferenceFragmentCompatHelper;
import com.bytehamster.lib.preferencesearch.PreferencesGraphProvider;
import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.bytehamster.preferencesearch.R;

import java.util.Set;
import java.util.stream.Collectors;

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
        config.setPreferenceFragmentsSupplier(this::getPreferenceFragments);
        config.setBreadcrumbsEnabled(true);
        config.setHistoryEnabled(true);
        config.setFuzzySearchEnabled(true);
    }

    private Set<Class<? extends PreferenceFragmentCompat>> getPreferenceFragments() {
        return new PreferencesGraphProvider(new PreferenceFragmentCompatHelper(getActivity()))
                .getPreferencesGraph(new PrefsFragmentFirst())
                .vertexSet()
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.host)
                .collect(Collectors.toSet());
    }
}
