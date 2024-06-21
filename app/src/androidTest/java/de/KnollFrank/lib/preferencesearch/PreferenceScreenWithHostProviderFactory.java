package de.KnollFrank.lib.preferencesearch;

import androidx.fragment.app.FragmentActivity;

import de.KnollFrank.preferencesearch.test.TestActivity;

public class PreferenceScreenWithHostProviderFactory {

    public static PreferenceScreenWithHostProvider createPreferenceScreenWithHostProvider(final FragmentActivity activity) {
        return new PreferenceScreenWithHostProvider(
                activity,
                new FragmentInitializer(
                        activity.getSupportFragmentManager(),
                        TestActivity.FRAGMENT_CONTAINER_VIEW));
    }
}
