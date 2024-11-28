package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LongRunningTaskWithProgressContainer<V> extends AsyncTask<Void, Void, V> {

    private final Callable<V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final View progressContainer;
    private final OnUiThreadRunner onUiThreadRunner;

    public LongRunningTaskWithProgressContainer(final Callable<V> doInBackground,
                                                final Consumer<V> onPostExecute,
                                                final View progressContainer,
                                                final OnUiThreadRunner onUiThreadRunner) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
        this.progressContainer = progressContainer;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    @Override
    protected void onPreExecute() {
        setVisibilityOfProgressContainer(View.VISIBLE);
    }

    @Override
    protected V doInBackground(final Void... voids) {
        try {
            return doInBackground.call();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(final V result) {
        setVisibilityOfProgressContainer(View.GONE);
        onPostExecute.accept(result);
    }

    private void setVisibilityOfProgressContainer(final int visible) {
        onUiThreadRunner.runNonBlockingOnUiThread(() -> progressContainer.setVisibility(visible));
    }
}
