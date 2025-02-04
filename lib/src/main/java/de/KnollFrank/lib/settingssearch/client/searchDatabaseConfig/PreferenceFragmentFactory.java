package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;

public abstract class PreferenceFragmentFactory<F extends Fragment, P extends PreferenceFragmentCompat> {

    public final FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection;

    public PreferenceFragmentFactory(final FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection) {
        this.fragmentWithPreferenceFragmentConnection = fragmentWithPreferenceFragmentConnection;
    }

    public <T extends Fragment> Optional<T> createPreferenceFragmentForClass(
            final Class<T> clazz,
            final Optional<PreferenceWithHost> src,
            final Context context,
            final IFragments fragments) {
        return canCreatePreferenceFragmentHavingClass(clazz) ?
                Optional.of((T) createPreferenceFragmentAndInitializeWithFragment(src, context, fragments)) :
                Optional.empty();
    }

    protected abstract void initializePreferenceFragmentWithFragment(P preferenceFragment, F fragment);

    private boolean canCreatePreferenceFragmentHavingClass(final Class<? extends Fragment> clazz) {
        return fragmentWithPreferenceFragmentConnection.preferenceFragment().equals(clazz);
    }

    private P createPreferenceFragmentAndInitializeWithFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        final P preferenceFragment = createPreferenceFragment(src, context, fragments);
        initializePreferenceFragmentWithFragment(preferenceFragment, getFragment(fragments));
        return preferenceFragment;
    }

    private P createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        return new DefaultFragmentFactory().instantiate(
                fragmentWithPreferenceFragmentConnection.preferenceFragment(),
                src,
                context,
                fragments);
    }

    private F getFragment(final IFragments fragments) {
        return fragments.instantiateAndInitializeFragment(
                fragmentWithPreferenceFragmentConnection.fragment(),
                Optional.empty());
    }
}
