package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.FragmentOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;

class FragmentFactoryInitializingPreferenceFragmentWithActivityDescriptionBeforeOnCreate implements FragmentFactory {

    private final FragmentFactory delegate;

    public FragmentFactoryInitializingPreferenceFragmentWithActivityDescriptionBeforeOnCreate(final FragmentFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T extends Fragment> FragmentOfActivity<T> instantiate(
            final FragmentClassOfActivity<T> fragmentClass,
            final Optional<PreferenceOfHostOfActivity> src,
            final Context context,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment) {
        final FragmentOfActivity<T> fragment = delegate.instantiate(fragmentClass, src, context, instantiateAndInitializeFragment);
        if (fragment.fragment() instanceof final InitializePreferenceFragmentWithActivityDescriptionBeforeOnCreate initializePreferenceFragmentWithActivityDescriptionBeforeOnCreate) {
            initializePreferenceFragmentWithActivityDescriptionBeforeOnCreate.initializePreferenceFragmentWithActivityDescriptionBeforeOnCreate(fragment.activityOfFragment());
        }
        return fragment;
    }
}
