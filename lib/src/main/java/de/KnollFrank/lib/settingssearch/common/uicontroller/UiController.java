package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.concurrent.CountDownLatch;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;

public class UiController {

    public static void waitUntilIdle() throws InterruptedException {
        final Activity activity = CurrentActivityProvider.awaitResumedActivity();
        waitUntilMainLooperIsIdle(activity);
        waitUntilLayoutIsStable(activity);
    }

    private static void waitUntilMainLooperIsIdle(final Activity activity) throws InterruptedException {
        final CountDownLatch looperLatch = new CountDownLatch(1);
        OnUiThreadRunnerFactory
                .fromActivity(activity)
                .runNonBlockingOnUiThread(
                        () ->
                                Looper
                                        .myQueue()
                                        .addIdleHandler(() -> {
                                            looperLatch.countDown();
                                            return false;
                                        }));
        looperLatch.await();
    }

    private static void waitUntilLayoutIsStable(final Activity activity) throws InterruptedException {
        final CountDownLatch layoutIsStableLatch = new CountDownLatch(1);
        final boolean pending =
                OnUiThreadRunnerFactory
                        .fromActivity(activity)
                        .runBlockingOnUiThread(() -> isLayoutOfViewPending(activity.getWindow().getDecorView(), layoutIsStableLatch));
        if (pending) {
            layoutIsStableLatch.await();
        }
    }

    private static boolean isLayoutOfViewPending(final View view, final CountDownLatch layoutIsStableLatch) {
        if (!view.isLayoutRequested()) {
            return false;
        }
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                        layoutIsStableLatch.countDown();
                    }
                });
        return true;
    }
}