package de.KnollFrank.lib.settingssearch.fragment;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class PreferencePathNavigator {

    private final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference;
    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap;
    private final Context context;

    public PreferencePathNavigator(final Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference,
                                   final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                                   final Context context) {
        this.hostByPreference = hostByPreference;
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.pojoEntityMap = pojoEntityMap;
        this.context = context;
    }

    public PreferenceFragmentCompat navigatePreferencePath(final PreferencePath preferencePath) {
        return navigatePreferences(preferencePath.preferences(), null, null);
    }

    private PreferenceFragmentCompat navigatePreferences(final List<? extends Preference> preferences,
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
                        instantiateAndInitializePreferenceFragment(getHost(preference), src)),
                instantiateFragment(getHost(preference), src));
    }

    private Class<? extends PreferenceFragmentCompat> getHost(final Preference preference) {
        return hostByPreference.get(getPojo(preference));
    }

    private SearchablePreferencePOJO getPojo(final Preference preference) {
        return pojoEntityMap.inverse().get(preference);
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
