package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.content.Context;

import de.KnollFrank.lib.settingssearch.fragment.FragmentFactoryAndInitializer;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragment;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializerProvider;

public class PreferencePathNavigatorFactory {

    public static PreferencePathNavigator createPreferencePathNavigator(
            final Context context,
            final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
            final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
            final ActivityInitializerProvider activityInitializerProvider) {
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
                        activityInitializerProvider,
                        preferenceWithHostProvider));
    }
}
