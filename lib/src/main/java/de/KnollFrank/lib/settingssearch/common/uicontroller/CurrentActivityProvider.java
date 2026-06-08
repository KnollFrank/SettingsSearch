package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;

public class CurrentActivityProvider {

    private static WeakReference<Activity> currentActivityRef = new WeakReference<>(null);
    private static final Object LOCK = new Object();

    public static void initialize(final Application application) {
        application.registerActivityLifecycleCallbacks(
                new Application.ActivityLifecycleCallbacks() {

                    @Override
                    public void onActivityResumed(final @NonNull Activity activity) {
                        synchronized (LOCK) {
                            currentActivityRef = new WeakReference<>(activity);
                            LOCK.notifyAll();
                        }
                    }

                    @Override
                    public void onActivityPaused(final @NonNull Activity activity) {
                        synchronized (LOCK) {
                            if (currentActivityRef.get() == activity) {
                                currentActivityRef.clear();
                            }
                        }
                    }

                    @Override
                    public void onActivityCreated(final @NonNull Activity activity, final @Nullable Bundle s) {
                    }

                    @Override
                    public void onActivityStarted(final @NonNull Activity activity) {
                    }

                    @Override
                    public void onActivityStopped(final @NonNull Activity activity) {
                    }

                    @Override
                    public void onActivitySaveInstanceState(final @NonNull Activity activity, final @NonNull Bundle o) {
                    }

                    @Override
                    public void onActivityDestroyed(final @NonNull Activity activity) {
                    }
                });
    }

    public static Optional<Activity> getCurrentActivity() {
        synchronized (LOCK) {
            return Optional.ofNullable(currentActivityRef.get());
        }
    }

    public static Activity awaitResumedActivity() throws InterruptedException {
        synchronized (LOCK) {
            while (true) {
                final Optional<Activity> currentActivity = getCurrentActivity();
                if (currentActivity.isPresent()) {
                    return currentActivity.get();
                }
                LOCK.wait();
            }
        }
    }
}