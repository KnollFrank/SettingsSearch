package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference.DbDataProvider;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class PreferenceWithHostProvider {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final Context context;
    private final DbDataProvider dbDataProvider;

    public PreferenceWithHostProvider(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                      final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                      final Context context,
                                      final DbDataProvider dbDataProvider) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.context = context;
        this.dbDataProvider = dbDataProvider;
    }

    public PreferenceWithHost getPreferenceWithHost(final SearchablePreference preference,
                                                    final Optional<PreferenceWithHost> src) {
        final PreferenceFragmentCompat hostOfPreference =
                instantiateAndInitializePreferenceFragment(
                        preference
                                .getHost(dbDataProvider)
                                .getHost(),
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
