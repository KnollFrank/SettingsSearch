package de.KnollFrank.lib.settingssearch.common.uicontroller;

import android.app.Activity;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.ExecutionException;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunnerFactory;

public class UiController {

    public static void waitUntilIdle() throws InterruptedException {
        final Activity activity = CurrentActivityProvider.awaitResumedActivity();
        waitUntilMainLooperIsIdle(activity);
        waitUntilLayoutIsStable(activity);
    }

    private static void waitUntilMainLooperIsIdle(final Activity activity) throws InterruptedException {
        final SettableFuture<Boolean> looperFuture = SettableFuture.create();
        OnUiThreadRunnerFactory
                .fromActivity(activity)
                .runNonBlockingOnUiThread(
                        () ->
                                Looper
                                        .myQueue()
                                        .addIdleHandler(() -> {
                                            looperFuture.set(true);
                                            return false;
                                        }));
        await(looperFuture);
    }

    private static void waitUntilLayoutIsStable(final Activity activity) throws InterruptedException {
        final SettableFuture<Boolean> layoutOfViewIsStableFuture = SettableFuture.create();
        if (isLayoutOfViewPending(activity, layoutOfViewIsStableFuture)) {
            await(layoutOfViewIsStableFuture);
        }
    }

    private static boolean isLayoutOfViewPending(final Activity activity, final SettableFuture<Boolean> layoutOfViewIsStableFuture) {
        return OnUiThreadRunnerFactory
                .fromActivity(activity)
                .runBlockingOnUiThread(
                        () ->
                                _isLayoutOfViewPending(
                                        activity.getWindow().getDecorView(),
                                        layoutOfViewIsStableFuture));
    }

    private static boolean _isLayoutOfViewPending(final View view, final SettableFuture<Boolean> layoutOfViewIsStableFuture) {
        if (!view.isLayoutRequested()) {
            return false;
        }
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                        layoutOfViewIsStableFuture.set(true);
                    }
                });
        return true;
    }

    private static <V> void await(final SettableFuture<V> future) throws InterruptedException {
        try {
            future.get();
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}