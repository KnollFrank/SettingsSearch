package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Bundles;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializerProvider;

class ContinueNavigationInActivity {

    private final Context context;
    private final ActivityInitializerProvider activityInitializerProvider;
    private final PreferenceWithHostProvider preferenceWithHostProvider;

    public ContinueNavigationInActivity(final Context context,
                                        final ActivityInitializerProvider activityInitializerProvider,
                                        final PreferenceWithHostProvider preferenceWithHostProvider) {
        this.context = context;
        this.activityInitializerProvider = activityInitializerProvider;
        this.preferenceWithHostProvider = preferenceWithHostProvider;
    }

    public Optional<PreferenceFragmentCompat> continueNavigationInActivity(final Class<? extends Activity> activity,
                                                                           final PreferencePathPointer preferencePathPointer,
                                                                           final Optional<PreferenceWithHost> src) {
        final Optional<PreferencePathPointer> nextPreferencePathPointer = preferencePathPointer.next();
        final PreferenceFragmentCompat host =
                preferenceWithHostProvider
                        .getPreferenceWithHost(preferencePathPointer.dereference(), src)
                        .host();
        if (nextPreferencePathPointer.isPresent()) {
            continueNavigationInActivity(activity, host, nextPreferencePathPointer.get());
            return Optional.empty();
        }
        return Optional.of(host);
    }

    private void continueNavigationInActivity(final Class<? extends Activity> activity,
                                              final PreferenceFragmentCompat src,
                                              final PreferencePathPointer preferencePathPointer) {
        beforeStartActivity(activity, src);
        startActivity(activity, src, preferencePathPointer);
    }

    private void beforeStartActivity(final Class<? extends Activity> activity,
                                     final PreferenceFragmentCompat src) {
        activityInitializerProvider
                .getActivityInitializerForActivity(activity)
                .ifPresent(activityInitializer -> activityInitializer.beforeStartActivity(src));
    }

    private void startActivity(final Class<? extends Activity> activity,
                               final PreferenceFragmentCompat src,
                               final PreferencePathPointer preferencePathPointer) {
        context.startActivity(createIntent(activity, src, preferencePathPointer));
    }

    private Intent createIntent(final Class<? extends Activity> activity,
                                final PreferenceFragmentCompat src,
                                final PreferencePathPointer preferencePathPointer) {
        final Intent intent = new Intent(context, activity);
        intent.putExtras(
                Bundles.merge(
                        activityInitializerProvider
                                .getActivityInitializerForActivity(activity)
                                .flatMap(activityInitializer -> activityInitializer.createExtras(src))
                                .orElseGet(Bundle::new),
                        PreferencePathNavigatorDataConverter.toBundle(
                                new PreferencePathNavigatorData(
                                        preferencePathPointer.preferencePath.getPreference().getId(),
                                        preferencePathPointer.indexWithinPreferencePath))));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }
}
