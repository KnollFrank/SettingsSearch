package de.KnollFrank.lib.settingssearch.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Bundles;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.ExtrasForActivityFactory;

public class PreferencePathNavigator {

    private final FragmentFactoryAndInitializer fragmentFactoryAndInitializer;
    private final Context context;
    private final InstantiateAndInitializeFragment instantiateAndInitializeFragment;
    private final ExtrasForActivityFactory extrasForActivityFactory;

    public PreferencePathNavigator(final FragmentFactoryAndInitializer fragmentFactoryAndInitializer,
                                   final Context context,
                                   final InstantiateAndInitializeFragment instantiateAndInitializeFragment,
                                   final ExtrasForActivityFactory extrasForActivityFactory) {
        this.fragmentFactoryAndInitializer = fragmentFactoryAndInitializer;
        this.context = context;
        this.instantiateAndInitializeFragment = instantiateAndInitializeFragment;
        this.extrasForActivityFactory = extrasForActivityFactory;
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
                continueNavigationInActivity(
                        activity.orElseThrow(),
                        preferencePathPointer,
                        src) :
                navigatePreferences(
                        preferencePathPointer.next(),
                        getPreferenceWithHost(preferencePathPointer.dereference(), src));
    }

    private Optional<PreferenceFragmentCompat> navigatePreferences(final Optional<PreferencePathPointer> preferencePathPointer,
                                                                   final PreferenceWithHost src) {
        return preferencePathPointer.isEmpty() ?
                Optional.of(src.host()) :
                navigatePreferences(preferencePathPointer.get(), Optional.of(src));
    }

    private Optional<PreferenceFragmentCompat> continueNavigationInActivity(final Class<? extends Activity> activity,
                                                                            final PreferencePathPointer preferencePathPointer,
                                                                            final Optional<PreferenceWithHost> src) {
        final Optional<PreferencePathPointer> nextPreferencePathPointer = preferencePathPointer.next();
        if (nextPreferencePathPointer.isPresent()) {
            continueNavigationInActivity(activity, nextPreferencePathPointer.get());
            return Optional.empty();
        }
        return Optional.of(getPreferenceWithHost(preferencePathPointer.dereference(), src).host());
    }

    private void continueNavigationInActivity(final Class<? extends Activity> activity,
                                              final PreferencePathPointer preferencePathPointer) {
        context.startActivity(createIntent(activity, preferencePathPointer));
    }

    private Intent createIntent(final Class<? extends Activity> activity,
                                final PreferencePathPointer preferencePathPointer) {
        final Intent intent = new Intent(context, activity);
        intent.putExtras(getExtras(activity, preferencePathPointer));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }

    private Bundle getExtras(final Class<? extends Activity> activity, final PreferencePathPointer preferencePathPointer) {
        return Bundles.merge(
                extrasForActivityFactory
                        .createExtrasForActivity(activity)
                        .orElseGet(Bundle::new),
                PreferencePathNavigatorDataConverter.toBundle(
                        new PreferencePathNavigatorData(
                                preferencePathPointer.preferencePath.getPreference().getId(),
                                preferencePathPointer.indexWithinPreferencePath)));
    }

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
