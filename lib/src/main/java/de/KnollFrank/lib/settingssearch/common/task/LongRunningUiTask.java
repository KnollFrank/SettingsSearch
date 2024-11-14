package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LongRunningUiTask<V> extends AsyncTask<Void, Void, V> {

    private final Callable<V> calculateUiResult;
    private final Consumer<V> doWithUiResult;
    private final OnUiThreadRunner onUiThreadRunner;
    private final ProgressBar progressBar;

    public LongRunningUiTask(final Callable<V> calculateUiResult,
                             final Consumer<V> doWithUiResult,
                             final OnUiThreadRunner onUiThreadRunner,
                             final ProgressBar progressBar) {
        this.calculateUiResult = calculateUiResult;
        this.doWithUiResult = doWithUiResult;
        this.onUiThreadRunner = onUiThreadRunner;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected V doInBackground(final Void... voids) {
        return onUiThreadRunner.runOnUiThread(calculateUiResult);
    }

    @Override
    protected void onPostExecute(final V result) {
        progressBar.setVisibility(View.GONE);
        doWithUiResult.accept(result);
    }
}
