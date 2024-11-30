package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;

import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class LongRunningTaskWithProgressContainer<V> extends AsyncTask<Void, String, V> {

    private final Function<ProgressUpdateListener, V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final View progressContainer;
    private final OnUiThreadRunner onUiThreadRunner;
    private final ProgressUpdateListener progressUpdateListener;

    public LongRunningTaskWithProgressContainer(final Function<ProgressUpdateListener, V> doInBackground,
                                                final Consumer<V> onPostExecute,
                                                final View progressContainer,
                                                final OnUiThreadRunner onUiThreadRunner,
                                                final ProgressUpdateListener progressUpdateListener) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
        this.progressContainer = progressContainer;
        this.onUiThreadRunner = onUiThreadRunner;
        this.progressUpdateListener = progressUpdateListener;
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
        progressUpdateListener.onProgressUpdate(values[0]);
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
