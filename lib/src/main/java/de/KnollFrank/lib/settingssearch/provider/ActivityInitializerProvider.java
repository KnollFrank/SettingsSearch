package de.KnollFrank.lib.settingssearch.provider;

import android.app.Activity;

import java.util.Optional;

// FK-TODO: ersetzte dieses Interface durch eine Map<Class<? extends Activity>, ActivityInitializer> activityInitializerByActivity
@FunctionalInterface
public interface ActivityInitializerProvider {

    Optional<ActivityInitializer> getActivityInitializerForActivity(final Class<? extends Activity> activity);
}
