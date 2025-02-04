package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;

public record FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<F extends Fragment, P extends PreferenceFragmentCompat>(
        FragmentWithPreferenceFragmentConnection<F, P> fragmentWithPreferenceFragmentConnection,
        PreferenceFragmentInitializer<P, F> preferenceFragmentInitializer) {

    public P createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        final P preferenceFragment = _createPreferenceFragment(src, context, fragments);
        initializePreferenceFragmentWithFragment(preferenceFragment, getFragment(fragments));
        return preferenceFragment;
    }

    private P _createPreferenceFragment(final Optional<PreferenceWithHost> src, final Context context, final IFragments fragments) {
        return new DefaultFragmentFactory().instantiate(
                fragmentWithPreferenceFragmentConnection().preferenceFragment(),
                src,
                context,
                fragments);
    }

    private F getFragment(final IFragments fragments) {
        return fragments.instantiateAndInitializeFragment(
                fragmentWithPreferenceFragmentConnection().fragment(),
                Optional.empty());
    }

    private void initializePreferenceFragmentWithFragment(final P preferenceFragment, final F fragment) {
        this
                .preferenceFragmentInitializer()
                .initializePreferenceFragmentWithFragment(
                        preferenceFragment,
                        fragment);
    }
}
