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
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferencePathNavigator {

    private final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;

    public PreferencePathNavigator(final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                   final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context) {
        this.hostByPreference = hostByPreference;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
    }

    public PreferenceFragmentCompat navigatePreferencePath(final PreferencePath preferencePath) {
        return navigatePreferences(preferencePath.preferences(), Optional.empty());
    }

    private PreferenceFragmentCompat navigatePreferences(final List<SearchablePreferencePOJO> preferences,
                                                         final Optional<PreferenceWithHost> src) {
        return preferences.isEmpty() ?
                src.orElseThrow().host() :
                navigatePreferences(
                        Lists.tail(preferences),
                        Optional.of(getPreferenceWithHost(Lists.head(preferences), src)));
    }

    private PreferenceWithHost getPreferenceWithHost(final SearchablePreferencePOJO preference,
                                                     final Optional<PreferenceWithHost> src) {
        final Class<? extends PreferenceFragmentCompat> host = hostByPreference.get(preference);
        final PreferenceFragmentCompat hostOfPreference = instantiateAndInitializePreferenceFragment(host, src);
        return new PreferenceWithHost(
                getPreferenceByKey(hostOfPreference, preference.key().orElseThrow()),
                hostOfPreference);
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceWithHost> src) {
        return (PreferenceFragmentCompat) fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment.getName(),
                src,
                context);
    }

    private static Preference getPreferenceByKey(final PreferenceFragmentCompat preferenceFragment,
                                                 final String key) {
        final Preference preference = preferenceFragment.findPreference(key);
        if (preference == null) {
            throw new IllegalArgumentException("can not find preference with key " + key + " within preferenceFragment " + preferenceFragment);
        }
        return preference;
    }
}
