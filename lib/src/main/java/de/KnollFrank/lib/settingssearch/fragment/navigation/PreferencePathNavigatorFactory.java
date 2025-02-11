package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentWithPreferenceFragmentConnection;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;

public class PreferencePathNavigatorFactory {

    public static PreferencePathNavigator createPreferencePathNavigator(
            final Context context,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
            final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        final PreferenceWithHostProvider preferenceWithHostProvider =
                new PreferenceWithHostProvider(
                        fragmentFactoryAndInitializer,
                        instantiateAndInitializeFragment,
                        context);
        return new PreferencePathNavigator(
                context,
                preferenceWithHostProvider,
                new ContinueNavigationInActivity(
                        context,
                        activityInitializerByActivity,
                        preferenceWithHostProvider),
                new ConnectedFragmentProvider(
                        getFragmentsConnected2PreferenceFragments(fragmentWithPreferenceFragmentConnections),
                        instantiateAndInitializeFragment));
    }

    private static Map<Class<? extends PreferenceFragmentCompat>, Class<? extends Fragment>> getFragmentsConnected2PreferenceFragments(final Set<FragmentWithPreferenceFragmentConnection<? extends Fragment, ? extends PreferenceFragmentCompat>> fragmentWithPreferenceFragmentConnections) {
        return fragmentWithPreferenceFragmentConnections
                .stream()
                .collect(
                        Collectors.toMap(
                                FragmentWithPreferenceFragmentConnection::preferenceFragment,
                                FragmentWithPreferenceFragmentConnection::fragment));
    }
}
