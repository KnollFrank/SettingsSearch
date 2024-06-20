package de.KnollFrank.lib.preferencesearch.provider;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.PreferenceScreensProvider;

class PreferenceScreenWithHostProvider {

    private final String preferenceFragment;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final Context context;

    public PreferenceScreenWithHostProvider(
            final String preferenceFragment,
            final PreferenceScreensProvider preferenceScreensProvider,
            final Context context) {
        this.preferenceFragment = preferenceFragment;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.context = context;
    }

    public List<PreferenceScreenWithHost> getPreferenceScreenWithHostList() {
        return new ArrayList<>(preferenceScreensProvider.getPreferenceScreens(instantiatePreferenceFragment()));
    }

    private PreferenceFragmentCompat instantiatePreferenceFragment() {
        return (PreferenceFragmentCompat) Fragment.instantiate(context, preferenceFragment);
    }
}
