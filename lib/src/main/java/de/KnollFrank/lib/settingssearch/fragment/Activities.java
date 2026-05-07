package de.KnollFrank.lib.settingssearch.fragment;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Activities {

    public static Optional<Activity> getCurrentActivity() {
        final AtomicReference<Optional<Activity>> currentActivity = new AtomicReference<>(Optional.empty());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> currentActivity.set(_getCurrentActivity()));
        return currentActivity.get();
    }

    private static Optional<Activity> _getCurrentActivity() {
        return getResumedActivities().stream().findFirst();
    }

    private static Collection<Activity> getResumedActivities() {
        return ActivityLifecycleMonitorRegistry
                .getInstance()
                .getActivitiesInStage(Stage.RESUMED);
    }
}