package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Lists;

public class PreferencePathNavigator {

    private final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;

    public PreferencePathNavigator(final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                   final FragmentFactoryAndInitializer fragmentFactoryAndInitializer) {
        this.hostByPreference = hostByPreference;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
    }

    public PreferenceFragmentCompat navigatePreferencePath(final PreferencePath preferencePath,
                                                           final Context context) {
        return navigatePreferences(
                preferencePath.preferences(),
                Optional.empty(),
                context);
    }

    private PreferenceFragmentCompat navigatePreferences(final List<Preference> preferences,
                                                         final Optional<PreferenceWithHost> src,
                                                         final Context context) {
        if (preferences.isEmpty()) {
            return src.get().host();
        }
        final Preference preference = Lists.head(preferences);
        return navigatePreferences(
                Lists.tail(preferences),
                Optional.of(
                        new PreferenceWithHost(
                                preference,
                                instantiateAndInitializePreferenceFragment(
                                        hostByPreference.get(preference),
                                        src,
                                        context))),
                context);
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceWithHost> src,
            final Context context) {
        return (PreferenceFragmentCompat) fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment.getName(),
                src,
                context);
    }
}
