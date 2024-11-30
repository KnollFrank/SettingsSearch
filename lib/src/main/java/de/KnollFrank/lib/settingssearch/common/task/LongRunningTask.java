package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class LongRunningTask<V> extends AsyncTask<Void, String, V> {

    private final Function<ProgressUpdateListener, V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final ProgressUpdateListener progressUpdateListener;

    public LongRunningTask(final Function<ProgressUpdateListener, V> doInBackground,
                           final Consumer<V> onPostExecute,
                           final ProgressUpdateListener progressUpdateListener) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
        this.progressUpdateListener = progressUpdateListener;
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
        onPostExecute.accept(result);
    }
}
