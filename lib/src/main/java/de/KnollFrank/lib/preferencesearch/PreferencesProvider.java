package de.KnollFrank.lib.preferencesearch;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PreferencesProvider implements IPreferencesProvider<PreferenceWrapper> {

    private final String preferenceFragment;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final Context context;

    public PreferencesProvider(final String preferenceFragment,
                               final PreferenceScreensProvider preferenceScreensProvider,
                               final Context context) {
        this.preferenceFragment = preferenceFragment;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.context = context;
    }

    @Override
    public List<PreferenceWrapper> getPreferences() {
        return preferenceScreensProvider
                .getPreferenceScreens(instantiatePreferenceFragment())
                .stream()
                .map(preferenceScreenWithHost -> preferenceScreenWithHost.preferenceScreen)
                .map(PreferenceProvider::getPreferences)
                .flatMap(Collection::stream)
                .map(PreferenceWrapper::new)
                .collect(Collectors.toList());
    }

    private PreferenceFragmentCompat instantiatePreferenceFragment() {
        return (PreferenceFragmentCompat) Fragment.instantiate(context, preferenceFragment);
    }
}
