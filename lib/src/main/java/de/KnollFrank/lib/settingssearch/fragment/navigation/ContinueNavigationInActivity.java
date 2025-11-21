package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.PreferenceWithHost;
import de.KnollFrank.lib.settingssearch.common.Bundles;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
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

    public Optional<PreferenceWithHost> continueNavigationInActivity(final Class<? extends Activity> activity,
                                                                     final PreferencePath preferencePath,
                                                                     final Optional<PreferenceWithHost> src) {
        final Optional<PreferencePath> tailPreferencePath = preferencePath.getTail();
        final PreferenceWithHost preferenceWithHost =
                preferenceWithHostProvider.getPreferenceWithHost(
                        preferencePath.getStart(),
                        src);
        if (tailPreferencePath.isPresent()) {
            continueNavigationInActivity(activity, preferenceWithHost.host(), tailPreferencePath.orElseThrow());
            return Optional.empty();
        }
        return Optional.of(preferenceWithHost);
    }

    private void continueNavigationInActivity(final Class<? extends Activity> activity,
                                              final PreferenceFragmentCompat src,
                                              final PreferencePath preferencePath) {
        beforeStartActivity(activity, src);
        startActivity(activity, src, preferencePath);
    }

    private <T extends PreferenceFragmentCompat> void beforeStartActivity(final Class<? extends Activity> activity,
                                                                          final T src) {
        Maps
                .get(activityInitializerByActivity, activity)
                .ifPresent(activityInitializer -> ((ActivityInitializer<T>) activityInitializer).beforeStartActivity(src));
    }

    private void startActivity(final Class<? extends Activity> activity,
                               final PreferenceFragmentCompat src,
                               final PreferencePath preferencePath) {
        context.startActivity(createIntent(activity, src, preferencePath));
    }

    private <T extends PreferenceFragmentCompat> Intent createIntent(final Class<? extends Activity> activity,
                                                                     final T src,
                                                                     final PreferencePath preferencePath) {
        final Intent intent = new Intent(context, activity);
        intent.putExtras(
                Bundles.merge(
                        createExtrasForActivity(activity, src),
                        getPreferencePathData(preferencePath)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }

    private <T extends PreferenceFragmentCompat> Bundle createExtrasForActivity(final Class<? extends Activity> activity,
                                                                                final T src) {
        return Maps
                .get(activityInitializerByActivity, activity)
                .flatMap(activityInitializer -> ((ActivityInitializer<T>) activityInitializer).createExtras(src))
                .orElseGet(Bundle::new);
    }

    private static Bundle getPreferencePathData(final PreferencePath preferencePath) {
        return PreferencePathDataConverter.toBundle(
                new PreferencePathData(
                        preferencePath
                                .preferences()
                                .stream()
                                .map(SearchablePreference::getId)
                                .collect(Collectors.toList())));
    }
}
