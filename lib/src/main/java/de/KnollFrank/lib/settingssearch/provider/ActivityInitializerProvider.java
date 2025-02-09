package de.KnollFrank.lib.settingssearch.provider;

import android.app.Activity;

import java.util.Optional;

@FunctionalInterface
public interface ActivityInitializerProvider {

    Optional<ActivityInitializer> getActivityInitializerForActivity(final Class<? extends Activity> activity);
}
