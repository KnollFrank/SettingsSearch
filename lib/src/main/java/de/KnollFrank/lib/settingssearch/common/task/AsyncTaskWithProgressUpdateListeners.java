package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class AsyncTaskWithProgressUpdateListeners<Params, Result> extends AsyncTask<Params, String, Result> {

    private final BiFunction<Params, ProgressUpdateListener, Result> doInBackground;
    private final Consumer<Result> onPostExecute;
    private final List<ProgressUpdateListener> progressUpdateListeners = new ArrayList<>();

    public AsyncTaskWithProgressUpdateListeners(final BiFunction<Params, ProgressUpdateListener, Result> doInBackground,
                                                final Consumer<Result> onPostExecute) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
    }

    public void addProgressUpdateListener(final ProgressUpdateListener progressUpdateListener) {
        synchronized (progressUpdateListeners) {
            progressUpdateListeners.add(progressUpdateListener);
        }
    }

    public void removeProgressUpdateListener(final ProgressUpdateListener progressUpdateListener) {
        synchronized (progressUpdateListeners) {
            progressUpdateListeners.remove(progressUpdateListener);
        }
    }

    @Override
    protected Result doInBackground(final Params... params) {
        try {
            return doInBackground.apply(
                    params.length > 0 ? params[0] : null,
                    this::publishProgress);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onProgressUpdate(final String... values) {
        synchronized (progressUpdateListeners) {
            for (final ProgressUpdateListener progressUpdateListener : progressUpdateListeners) {
                progressUpdateListener.onProgressUpdate(values[0]);
            }
        }
    }

    @Override
    protected void onPostExecute(final Result result) {
        onPostExecute.accept(result);
    }
}
