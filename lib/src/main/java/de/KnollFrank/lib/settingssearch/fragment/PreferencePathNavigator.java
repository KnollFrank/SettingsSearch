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
    private final Context context;

    public PreferencePathNavigator(final Map<Preference, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                   final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context) {
        this.hostByPreference = hostByPreference;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public PreferenceFragmentCompat navigatePreferencePath(final PreferencePath preferencePath) {
        return navigatePreferences(preferencePath.preferences(), null, null);
    }

    private PreferenceFragmentCompat navigatePreferences(final List<Preference> preferences,
                                                         final PreferenceWithHost src,
                                                         final PreferenceFragmentCompat uninitializedSrc) {
        if (preferences.isEmpty()) {
            return uninitializedSrc;
        }
        final Preference preference = Lists.head(preferences);
        return navigatePreferences(
                Lists.tail(preferences),
                new PreferenceWithHost(
                        preference,
                        instantiateAndInitializePreferenceFragment(
                                hostByPreference.get(preference),
                                src)),
                instantiateFragment(hostByPreference.get(preference), src));
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final PreferenceWithHost src) {
        return (PreferenceFragmentCompat) fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment.getName(),
                Optional.ofNullable(src),
                context);
    }

    private PreferenceFragmentCompat instantiateFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final PreferenceWithHost src) {
        return (PreferenceFragmentCompat) fragmentFactoryAndInitializer.instantiateFragment(
                preferenceFragment.getName(),
                Optional.ofNullable(src),
                context);
    }
}
