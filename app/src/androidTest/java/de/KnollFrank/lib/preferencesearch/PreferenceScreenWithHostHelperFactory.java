package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceScreenWithHostHelperFactory {

    public static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(final FragmentActivity activity) {
        return new PreferenceScreenWithHostProvider(
                activity,
                activity.getSupportFragmentManager(),
                TestActivity.FRAGMENT_CONTAINER_VIEW);
    }
}
