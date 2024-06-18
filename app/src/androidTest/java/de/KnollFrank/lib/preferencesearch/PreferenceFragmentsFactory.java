package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceFragmentsFactory {

    public static PreferenceFragments createPreferenceFragments(final FragmentActivity activity) {
        return new PreferenceFragments(
                activity,
                activity.getSupportFragmentManager(),
                TestActivity.FRAGMENT_CONTAINER_VIEW);
    }
}
