package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
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

    public PreferenceOfHost getPreferenceWithHost(final SearchablePreferenceOfHostWithinTree preference,
                                                  final Optional<PreferenceOfHost> src) {
        final PreferenceFragmentCompat hostOfPreference =
                instantiateAndInitializePreferenceFragment(
                        preference.hostOfPreference().host(),
                        src);
        return new PreferenceOfHost(
                Preferences.findPreferenceByKeyOrElseThrow(hostOfPreference, preference.searchablePreference().getKey()),
                hostOfPreference);
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceOfHost> src) {
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment,
                src,
                context,
                instantiateAndInitializeFragment);
    }
}
