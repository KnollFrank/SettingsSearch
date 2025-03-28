package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class AsyncTaskWithProgressUpdateListeners<V> extends AsyncTask<Void, String, V> {

    private final Function<ProgressUpdateListener, V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final List<ProgressUpdateListener> progressUpdateListeners = new ArrayList<>();

    public AsyncTaskWithProgressUpdateListeners(final Function<ProgressUpdateListener, V> doInBackground,
                                                final Consumer<V> onPostExecute) {
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
    protected V doInBackground(final Void... voids) {
        try {
            return doInBackground.apply(this::publishProgress);
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
    protected void onPostExecute(final V result) {
        onPostExecute.accept(result);
    }
}
