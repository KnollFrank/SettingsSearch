package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;

import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;

public class LongRunningTaskWithProgressContainer<V> extends AsyncTask<Void, String, V> {

    private final Function<IProgressDisplayer, V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final View progressContainer;
    private final OnUiThreadRunner onUiThreadRunner;
    private final IProgressDisplayer progressDisplayer;

    public LongRunningTaskWithProgressContainer(final Function<IProgressDisplayer, V> doInBackground,
                                                final Consumer<V> onPostExecute,
                                                final View progressContainer,
                                                final OnUiThreadRunner onUiThreadRunner,
                                                final IProgressDisplayer progressDisplayer) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
        this.progressContainer = progressContainer;
        this.onUiThreadRunner = onUiThreadRunner;
        this.progressDisplayer = progressDisplayer;
    }

    @Override
    protected void onPreExecute() {
        setVisibilityOfProgressContainer(View.VISIBLE);
    }

    @Override
    protected V doInBackground(final Void... voids) {
        try {
            return doInBackground.apply(this::publishProgress);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onProgressUpdate(final String... values) {
        progressDisplayer.displayProgress(values[0]);
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
