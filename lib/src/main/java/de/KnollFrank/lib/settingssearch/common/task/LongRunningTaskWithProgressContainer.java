package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class LongRunningTaskWithProgressContainer<V> extends AsyncTask<Void, String, V> {

    private final Function<ProgressUpdateListener, V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final View progressContainer;
    private final OnUiThreadRunner onUiThreadRunner;
    private final List<ProgressUpdateListener> progressUpdateListeners = new ArrayList<>();

    public LongRunningTaskWithProgressContainer(final Function<ProgressUpdateListener, V> doInBackground,
                                                final Consumer<V> onPostExecute,
                                                final View progressContainer,
                                                final OnUiThreadRunner onUiThreadRunner) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
        this.progressContainer = progressContainer;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    public void addProgressUpdateListener(final ProgressUpdateListener progressUpdateListener) {
        progressUpdateListeners.add(progressUpdateListener);
    }

    public void removeProgressUpdateListener(final ProgressUpdateListener progressUpdateListener) {
        progressUpdateListeners.remove(progressUpdateListener);
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
        for (final ProgressUpdateListener progressUpdateListener : progressUpdateListeners) {
            progressUpdateListener.onProgressUpdate(values[0]);
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
