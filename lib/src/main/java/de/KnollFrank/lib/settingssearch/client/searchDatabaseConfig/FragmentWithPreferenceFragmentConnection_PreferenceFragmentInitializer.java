package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;

public class FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<F extends Fragment, P extends PreferenceFragmentCompat> {

    public final FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection;
    private final PreferenceFragmentInitializer<P, F> preferenceFragmentInitializer;

    public FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer(
            final FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection,
            final PreferenceFragmentInitializer<P, F> preferenceFragmentInitializer) {
        this.fragmentWithPreferenceFragmentConnection = fragmentWithPreferenceFragmentConnection;
        this.preferenceFragmentInitializer = preferenceFragmentInitializer;
    }

    public boolean canCreatePreferenceFragmentHavingClass(final Class<? extends Fragment> clazz) {
        return fragmentWithPreferenceFragmentConnection.preferenceFragment().equals(clazz);
    }

    public P createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        final P preferenceFragment = _createPreferenceFragment(src, context, fragments);
        preferenceFragmentInitializer.initializePreferenceFragmentWithFragment(preferenceFragment, getFragment(fragments));
        return preferenceFragment;
    }

    private P _createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
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
