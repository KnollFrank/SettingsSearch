package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import static com.bytehamster.preferencesearch.multiplePreferenceScreens.MultiplePreferenceScreensExample.FRAGMENT_CONTAINER_VIEW;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.BaseSearchPreferenceFragment;
import com.bytehamster.lib.preferencesearch.PreferenceFragments;
import com.bytehamster.lib.preferencesearch.PreferenceScreensProvider;
import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.bytehamster.preferencesearch.R;

import java.util.Set;
import java.util.stream.Collectors;

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

    public static Set<Class<? extends PreferenceFragmentCompat>> getPreferenceFragments(final PreferenceFragmentCompat root, final FragmentActivity fragmentActivity, @IdRes final int containerResId) {
        return new PreferenceScreensProvider(new PreferenceFragments(fragmentActivity, containerResId))
                .getPreferenceScreens(root)
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.host)
                .collect(Collectors.toSet());
    }
}
