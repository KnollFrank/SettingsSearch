package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceOfHostWithinTree;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

// FK-TODO: rename to PreferenceOfHostOfActivityProvider
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

    public PreferenceOfHostOfActivity getPreferenceWithHost(final SearchablePreferenceOfHostWithinTree preference,
                                                            final Optional<PreferenceOfHostOfActivity> src) {
        final PreferenceFragmentCompat hostOfPreference =
                instantiateAndInitializePreferenceFragment(
                        preference
                                .hostOfPreference()
                                .host()
                                .fragment(),
                        src);
        return new PreferenceOfHostOfActivity(
                Preferences.findPreferenceByKeyOrElseThrow(hostOfPreference, preference.searchablePreference().getKey()),
                hostOfPreference,
                preference.hostOfPreference().host().activityOFragment());
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceOfHostOfActivity> src) {
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment,
                src,
                context,
                instantiateAndInitializeFragment);
    }
}
