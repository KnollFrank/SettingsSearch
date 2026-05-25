package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentToPreferencesConverter;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.graph.PreferencesOfFragment;

class PreferenceOfHostOfActivityProvider {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final Context context;
    private final FragmentToPreferencesConverter fragmentToPreferencesConverter;

    public PreferenceOfHostOfActivityProvider(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                              final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                              final Context context,
                                              final FragmentToPreferencesConverter fragmentToPreferencesConverter) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.context = context;
        this.fragmentToPreferencesConverter = fragmentToPreferencesConverter;
    }

    public PreferenceOfHostOfActivity getPreferenceOfHostOfActivity(final SearchablePreferenceOfHostWithinTree preference,
                                                                    final Optional<PreferenceOfHostOfActivity> src) {
        final FragmentOfActivity<? extends Fragment> hostOfPreference =
                instantiateAndInitializeFragment(
                        preference.hostOfPreference().host(),
                        src);
        final PreferencesOfFragment preferencesOfFragment = fragmentToPreferencesConverter.getPreferences(hostOfPreference.fragment()).orElseThrow();
        return new PreferenceOfHostOfActivity(
                Preferences.findPreferenceByKeyOrElseThrow(
                        preferencesOfFragment.preferences(),
                        preference.searchablePreference().getKey()),
                hostOfPreference.fragment(),
                hostOfPreference.activityOfFragment());
    }

    private FragmentOfActivity<? extends Fragment> instantiateAndInitializeFragment(
            final FragmentClassOfActivity<? extends Fragment> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src) {
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                fragmentClass,
                src,
                context,
                instantiateAndInitializeFragment);
    }
}
