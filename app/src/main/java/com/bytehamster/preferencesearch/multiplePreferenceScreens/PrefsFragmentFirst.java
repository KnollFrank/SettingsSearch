package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import static com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample.FRAGMENT_CONTAINER_VIEW;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.PreferenceFragmentHelper;
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
        config.setFragmentContainerViewId(FRAGMENT_CONTAINER_VIEW);
        config.setPreferenceFragmentsSupplier(() -> getPreferenceFragments(new PrefsFragmentFirst()));
        config.setBreadcrumbsEnabled(true);
        config.setHistoryEnabled(true);
        config.setFuzzySearchEnabled(true);
    }

    private Set<Class<? extends PreferenceFragmentCompat>> getPreferenceFragments(final PreferenceFragmentCompat root) {
        return new PreferencesGraphProvider(new PreferenceFragmentHelper(getActivity(), FRAGMENT_CONTAINER_VIEW))
                .getPreferencesGraph(root)
                .vertexSet()
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.host)
                .collect(Collectors.toSet());
    }
}
