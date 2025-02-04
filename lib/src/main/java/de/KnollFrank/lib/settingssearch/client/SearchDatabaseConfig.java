package de.KnollFrank.lib.settingssearch.client;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.Fragment2PreferenceFragmentConverter;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.IFragments;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceFragmentConnected2PreferenceProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PreferenceSearchablePredicate;
import de.KnollFrank.lib.settingssearch.provider.RootPreferenceFragmentOfActivityProvider;
import de.KnollFrank.lib.settingssearch.search.provider.IconResourceIdProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public record SearchDatabaseConfig(FragmentFactory fragmentFactory,
                                   IconResourceIdProvider iconResourceIdProvider,
                                   SearchableInfoProvider searchableInfoProvider,
                                   PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                   PreferenceFragmentConnected2PreferenceProvider preferenceFragmentConnected2PreferenceProvider,
                                   RootPreferenceFragmentOfActivityProvider rootPreferenceFragmentOfActivityProvider,
                                   PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                   PreferenceSearchablePredicate preferenceSearchablePredicate,
                                   Fragment2PreferenceFragmentConverter fragment2PreferenceFragmentConverter) {

    @FunctionalInterface
    public interface PreferenceFragmentInitializer<P extends PreferenceFragmentCompat, F extends Fragment> {

        void initializePreferenceFragmentWithFragment(P preferenceFragment, F fragment);
    }

    public record ActivitySearchDatabaseConfig<A extends Activity, F extends Fragment, P1 extends PreferenceFragmentCompat, P2 extends PreferenceFragmentCompat>(
            ActivityWithRootPreferenceFragmentConnection<A, P1> activityWithRootPreferenceFragmentConnection,
            Optional<FragmentWithPreferenceFragmentConnection_PreferenceFragmentInitializer<F, P2>> fragmentWithPreferenceFragmentConnection_preferenceFragmentInitializer) {
    }

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

    public record ActivityWithRootPreferenceFragmentConnection<A extends Activity, P extends PreferenceFragmentCompat>(
            Class<A> activityClass,
            Class<P> rootPreferenceFragmentClassOfActivityClass) {
    }

    public record FragmentWithPreferenceFragmentConnection<F extends Fragment, P extends PreferenceFragmentCompat>(
            Class<F> fragment,
            Class<P> preferenceFragment) {
    }
}
