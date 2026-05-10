package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.concurrent.CountDownLatch;

public class UiController {

    // FK-TODO: refactor
    public static void waitUntilIdle() throws InterruptedException {
        final Activity activity = CurrentActivityProvider.awaitResumedActivity();

        // 1. Warten bis der Main-Looper idle ist (MessageQueue leer)
        final CountDownLatch looperLatch = new CountDownLatch(1);
        activity.runOnUiThread(
                () ->
                        Looper.myQueue().addIdleHandler(
                                new MessageQueue.IdleHandler() {

                                    @Override
                                    public boolean queueIdle() {
                                        looperLatch.countDown();
                                        return false;
                                    }
                                }));
        looperLatch.await();

        // 2. Warten bis das Layout stabil ist
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
