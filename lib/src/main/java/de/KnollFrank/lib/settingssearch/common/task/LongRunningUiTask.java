package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LongRunningUiTask<V> extends AsyncTask<Void, Void, V> {

    private final Callable<V> calculateUiResult;
    private final Consumer<V> doWithUiResult;
    private final OnUiThreadRunner onUiThreadRunner;
    private final View progressContainer;

    public LongRunningUiTask(final Callable<V> calculateUiResult,
                             final Consumer<V> doWithUiResult,
                             final OnUiThreadRunner onUiThreadRunner,
                             final View progressContainer) {
        this.calculateUiResult = calculateUiResult;
        this.doWithUiResult = doWithUiResult;
        this.onUiThreadRunner = onUiThreadRunner;
        this.progressContainer = progressContainer;
    }

    @Override
    protected void onPreExecute() {
        progressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected V doInBackground(final Void... voids) {
        return onUiThreadRunner.runOnUiThread(calculateUiResult);
    }

    @Override
    protected void onPostExecute(final V result) {
        progressContainer.setVisibility(View.GONE);
        doWithUiResult.accept(result);
    }
}
