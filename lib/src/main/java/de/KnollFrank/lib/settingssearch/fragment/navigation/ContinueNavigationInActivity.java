package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Bundles;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.provider.ActivityInitializer;

class ContinueNavigationInActivity {

    private final Context context;
    private final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity;
    private final PreferenceWithHostProvider preferenceWithHostProvider;

    public ContinueNavigationInActivity(final Context context,
                                        final Map<Class<? extends Activity>, ActivityInitializer<?>> activityInitializerByActivity,
                                        final PreferenceWithHostProvider preferenceWithHostProvider) {
        this.context = context;
        this.activityInitializerByActivity = activityInitializerByActivity;
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

    private <T extends PreferenceFragmentCompat> void beforeStartActivity(
            final Class<? extends Activity> activity,
            final T src) {
        Maps
                .get(activityInitializerByActivity, activity)
                .ifPresent(activityInitializer -> ((ActivityInitializer<T>) activityInitializer).beforeStartActivity(src));
    }

    private void startActivity(final Class<? extends Activity> activity,
                               final PreferenceFragmentCompat src,
                               final PreferencePathPointer preferencePathPointer) {
        context.startActivity(createIntent(activity, src, preferencePathPointer));
    }

    private <T extends PreferenceFragmentCompat> Intent createIntent(
            final Class<? extends Activity> activity,
            final T src,
            final PreferencePathPointer preferencePathPointer) {
        final Intent intent = new Intent(context, activity);
        intent.putExtras(
                Bundles.merge(
                        Maps
                                .get(activityInitializerByActivity, activity)
                                .flatMap(activityInitializer -> ((ActivityInitializer<T>) activityInitializer).createExtras(src))
                                .orElseGet(Bundle::new),
                        PreferencePathNavigatorDataConverter.toBundle(
                                new PreferencePathNavigatorData(
                                        preferencePathPointer.preferencePath.getPreference().getId(),
                                        preferencePathPointer.indexWithinPreferencePath))));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }
}
