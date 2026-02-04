package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

import de.KnollFrank.lib.settingssearch.PrincipalAndProxyProvider;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;

public class PreferencePathNavigatorFactory {

    private PreferencePathNavigatorFactory() {
    }

    public static PreferencePathNavigator createPreferencePathNavigator(
            final Context context,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
            final PrincipalAndProxyProvider principalAndProxyProvider) {
        final PreferenceOfHostOfActivityProvider preferenceOfHostOfActivityProvider =
                new PreferenceOfHostOfActivityProvider(
                        fragmentFactoryAndInitializer,
                        instantiateAndInitializeFragment,
                        context);
        return new PreferencePathNavigator(
                context,
                preferenceOfHostOfActivityProvider,
                new ContinueNavigationInActivity(
                        context,
                        activityInitializerByActivity,
                        preferenceOfHostOfActivityProvider),
                new PrincipalProvider(
                        principalAndProxyProvider,
                        instantiateAndInitializeFragment));
    }
}
