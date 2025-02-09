package de.KnollFrank.lib.settingssearch.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializerProvider;

public class PreferencePathNavigator {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final ActivityInitializerProvider activityInitializerProvider;

    public PreferencePathNavigator(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context,
                                   final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                   final ActivityInitializerProvider activityInitializerProvider) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.activityInitializerProvider = activityInitializerProvider;
    }

    public Optional<PreferenceFragmentCompat> navigatePreferencePath(final PreferencePathPointer preferencePathPointer) {
        return navigatePreferences(preferencePathPointer, Optional.empty());
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final PreferencePathPointer preferencePathPointer,
                                                                   final Optional<PreferenceWithHost> src) {
        final Optional<Class<? extends Activity>> activity =
                preferencePathPointer
                        .dereference()
                        .getClassOfReferencedActivity(context);
        return activity.isPresent() ?
                continueNavigationInActivity(activity.orElseThrow(), preferencePathPointer, src) :
                navigatePreferences(
                        preferencePathPointer.next(),
                        getPreferenceWithHost(preferencePathPointer.dereference(), src));
    }

    private Optional<PreferenceFragmentCompat> continueNavigationInActivity(final Class<? extends Activity> activity,
                                                                            final PreferencePathPointer preferencePathPointer,
                                                                            final Optional<PreferenceWithHost> src) {
        final ContinueNavigationInActivity continueNavigationInActivity =
                new ContinueNavigationInActivity(
                        context,
                        activityInitializerProvider,
                        this::getPreferenceWithHost);
        return continueNavigationInActivity.continueNavigationInActivity(activity, preferencePathPointer, src);
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final Optional<PreferencePathPointer> preferencePathPointer,
                                                                   final PreferenceWithHost src) {
        return preferencePathPointer.isEmpty() ?
                Optional.of(src.host()) :
                navigatePreferences(preferencePathPointer.get(), Optional.of(src));
    }

    // FK-TODO: move this method and dependent methods into new class
    private PreferenceWithHost getPreferenceWithHost(final SearchablePreference preference,
                                                     final Optional<PreferenceWithHost> src) {
        final PreferenceFragmentCompat hostOfPreference = instantiateAndInitializePreferenceFragment(preference.getHost(), src);
        return new PreferenceWithHost(
                Preferences.findPreferenceOrElseThrow(hostOfPreference, preference.getKey().orElseThrow()),
                hostOfPreference);
    }

    private PreferenceFragmentCompat instantiateAndInitializePreferenceFragment(
            final Class<? extends PreferenceFragmentCompat> preferenceFragment,
            final Optional<PreferenceWithHost> src) {
        return fragmentFactoryAndInitializer.instantiateAndInitializeFragment(
                preferenceFragment,
                src,
                context,
                instantiateAndInitializeFragment);
    }
}
