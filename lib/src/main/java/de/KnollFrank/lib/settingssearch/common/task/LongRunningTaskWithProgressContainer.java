package de.KnollFrank.lib.settingssearch.common.task;

import android.view.View;

import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class LongRunningTaskWithProgressContainer<V> extends LongRunningTask<V> {

    private final View progressContainer;
    private final OnUiThreadRunner onUiThreadRunner;

    public LongRunningTaskWithProgressContainer(final Function<ProgressUpdateListener, V> doInBackground,
                                                final Consumer<V> onPostExecute,
                                                final View progressContainer,
                                                final OnUiThreadRunner onUiThreadRunner) {
        super(doInBackground, onPostExecute);
        this.progressContainer = progressContainer;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    @Override
    protected void onPreExecute() {
        setVisibilityOfProgressContainer(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final V result) {
        setVisibilityOfProgressContainer(View.GONE);
        super.onPostExecute(result);
    }

    private void setVisibilityOfProgressContainer(final int visible) {
        onUiThreadRunner.runNonBlockingOnUiThread(() -> progressContainer.setVisibility(visible));
    }
}
