package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;

public class FragmentFactoryFactory {

    private FragmentFactoryFactory() {
    }

    public static FragmentFactory createFragmentFactory(
            final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies,
            final FragmentFactory delegate) {
        return _createFragmentFactory(
                principalAndProxies,
                new FragmentFactoryInitializingPreferenceFragmentWithActivityDescriptionBeforeOnCreate(delegate));
    }

    private static PreferenceFragmentFactoriesWrapper _createFragmentFactory(
            final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies,
            final FragmentFactory delegate) {
        return new PreferenceFragmentFactoriesWrapper(
                createPreferenceFragmentFactories(principalAndProxies, delegate),
                delegate);
    }

    private static Set<PreferenceFragmentFactory<? extends Fragment, ? extends PreferenceFragmentCompat>> createPreferenceFragmentFactories(
            final Set<PrincipalAndProxy<? extends Fragment, ? extends PreferenceFragmentCompat>> principalAndProxies,
            final FragmentFactory delegate) {
        return principalAndProxies
                .stream()
                .map(principalAndProxy ->
                             new PreferenceFragmentFactory<>(
                                     principalAndProxy,
                                     delegate))
                .collect(Collectors.toSet());
    }
}
