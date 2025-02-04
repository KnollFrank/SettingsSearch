package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;

class FragmentFactory implements de.KnollFrank.lib.settingssearch.fragment.FragmentFactory {

    private final List<? extends PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories;
    private final de.KnollFrank.lib.settingssearch.fragment.FragmentFactory delegate;

    public FragmentFactory(final List<? extends PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> preferenceFragmentFactories,
                           final de.KnollFrank.lib.settingssearch.fragment.FragmentFactory delegate) {
        this.preferenceFragmentFactories = preferenceFragmentFactories;
        this.delegate = delegate;
    }

    @Override
    public <T extends Fragment> T instantiate(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        return this
                .createPreferenceFragment(fragmentClass, src, context, fragments)
                .orElseGet(() -> delegate.instantiate(fragmentClass, src, context, fragments));
    }

    private <T extends Fragment> Optional<T> createPreferenceFragment(final Class<T> fragmentClass, final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        return preferenceFragmentFactories
                .stream()
                .map(preferenceFragmentFactory -> preferenceFragmentFactory.createPreferenceFragmentForClass(fragmentClass, src, context, fragments))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
