package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.concurrent.CountDownLatch;

public class UiController {

    // FK-TODO: refactor
    public static void waitUntilIdle() throws InterruptedException {
        final Activity activity = CurrentActivityProvider.awaitResumedActivity();
        waitUntilMainLooperIsIdle(activity);
        waitUntilLayoutIsStable(activity);
    }

    private static void waitUntilMainLooperIsIdle(final Activity activity) throws InterruptedException {
        final CountDownLatch looperLatch = new CountDownLatch(1);
        activity.runOnUiThread(
                () ->
                        Looper.myQueue().addIdleHandler(() -> {
                            looperLatch.countDown();
                            return false;
                        }));
        looperLatch.await();
    }

    private static void waitUntilLayoutIsStable(final Activity activity) throws InterruptedException {
        final CountDownLatch layoutLatch = new CountDownLatch(1);
        activity.runOnUiThread(() -> {
            final View decorView = activity.getWindow().getDecorView();
            if (!decorView.isLayoutRequested()) {
                layoutLatch.countDown();
                return;
            }
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            layoutLatch.countDown();
                        }
                    });
        });
        layoutLatch.await();
    }
}
