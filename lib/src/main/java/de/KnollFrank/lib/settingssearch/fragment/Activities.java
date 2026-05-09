package de.KnollFrank.lib.settingssearch.fragment;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Optional;

public class Activities {

    private static WeakReference<Activity> currentActivityRef = new WeakReference<>(null);

    public static void initialize(final Application application) {
        application.registerActivityLifecycleCallbacks(
                new Application.ActivityLifecycleCallbacks() {

                    @Override
                    public void onActivityResumed(final @NonNull Activity activity) {
                        currentActivityRef = new WeakReference<>(activity);
                    }

                    @Override
                    public void onActivityPaused(final @NonNull Activity activity) {
                        if (currentActivityRef.get() == activity) {
                            currentActivityRef.clear();
                        }
                    }

                    @Override
                    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle s) {
                    }

                    @Override
                    public void onActivityStarted(@NonNull Activity activity) {
                    }

                    @Override
                    public void onActivityStopped(@NonNull Activity activity) {
                    }

                    @Override
                    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle o) {
                    }

                    @Override
                    public void onActivityDestroyed(@NonNull Activity activity) {
                    }
                });
    }

    public static Optional<Activity> getCurrentActivity() {
        return Optional.ofNullable(currentActivityRef.get());
    }
}