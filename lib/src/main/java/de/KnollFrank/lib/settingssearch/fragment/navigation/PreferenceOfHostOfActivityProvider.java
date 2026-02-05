package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class PreferenceOfHostOfActivityProvider {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final Context context;

    public PreferenceOfHostOfActivityProvider(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                              final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                              final Context context) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.context = context;
    }

    public PreferenceOfHostOfActivity getPreferenceOfHostOfActivity(final SearchablePreferenceOfHostWithinTree preference,
                                                                    final Optional<PreferenceOfHostOfActivity> src) {
        final FragmentOfActivity<? extends PreferenceFragmentCompat> hostOfPreference =
                instantiateAndInitializePreferenceFragment(
                        preference.hostOfPreference().host(),
                        src);
        return new PreferenceOfHostOfActivity(
                Preferences.findPreferenceByKeyOrElseThrow(
                        hostOfPreference.fragment(),
                        preference.searchablePreference().getKey()),
                hostOfPreference.fragment(),
                hostOfPreference.activityOfFragment());
    }

    private FragmentOfActivity<? extends PreferenceFragmentCompat> instantiateAndInitializePreferenceFragment(
            final FragmentClassOfActivity<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceOfHostOfActivity> src) {
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment,
                src,
                context,
                instantiateAndInitializeFragment);
    }
}
