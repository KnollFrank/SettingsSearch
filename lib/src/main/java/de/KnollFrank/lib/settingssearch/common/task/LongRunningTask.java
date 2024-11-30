package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.IProgressDisplayer;

public class LongRunningTask<V> extends AsyncTask<Void, String, V> {

    private final Function<IProgressDisplayer, V> doInBackground;
    private final Consumer<V> onPostExecute;
    private final IProgressDisplayer progressDisplayer;

    public LongRunningTask(final Function<IProgressDisplayer, V> doInBackground,
                           final Consumer<V> onPostExecute,
                           final IProgressDisplayer progressDisplayer) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
        this.progressDisplayer = progressDisplayer;
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
        onPostExecute.accept(result);
    }
}
