package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class PreferenceWithHostProvider {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final Context context;

    public PreferenceWithHostProvider(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                      final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                      final Context context) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.context = context;
    }

    public PreferenceWithHost getPreferenceWithHost(final SearchablePreference preference,
                                                    final Optional<PreferenceWithHost> src) {
        final PreferenceFragmentCompat hostOfPreference =
                instantiateAndInitializePreferenceFragment(
                        preference.getHost().host(),
                        src);
        return new PreferenceWithHost(
                Preferences.findPreferenceOrElseThrow(hostOfPreference, preference.getKey()),
                hostOfPreference);
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceWithHost> src) {
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment,
                src,
                context,
                instantiateAndInitializeFragment);
    }
}
