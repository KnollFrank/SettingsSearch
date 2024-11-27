package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class LongRunningTask<V> extends AsyncTask<Void, Void, V> {

    private final Callable<V> doInBackground;
    private final Consumer<V> onPostExecute;

    public LongRunningTask(final Callable<V> doInBackground, final Consumer<V> onPostExecute) {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
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
        onPostExecute.accept(result);
    }
}
