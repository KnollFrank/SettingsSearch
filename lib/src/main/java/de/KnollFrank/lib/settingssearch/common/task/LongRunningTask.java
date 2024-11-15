package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LongRunningTask<V> extends AsyncTask<Void, Void, V> {

    private final Callable<V> calculateUiResult;
    private final Consumer<V> doWithUiResult;
    // FK-TODO: pass onUiThreadRunner as a parameter to methods
    public static OnUiThreadRunner onUiThreadRunner;
    private final View progressContainer;

    public LongRunningTask(final Callable<V> calculateUiResult,
                           final Consumer<V> doWithUiResult,
                           final OnUiThreadRunner onUiThreadRunner,
                           final View progressContainer) {
        this.calculateUiResult = calculateUiResult;
        this.doWithUiResult = doWithUiResult;
        LongRunningTask.onUiThreadRunner = onUiThreadRunner;
        this.progressContainer = progressContainer;
    }

    @Override
    protected void onPreExecute() {
        progressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected V doInBackground(final Void... voids) {
        try {
            return calculateUiResult.call();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(final V result) {
        progressContainer.setVisibility(View.GONE);
        doWithUiResult.accept(result);
    }
}
