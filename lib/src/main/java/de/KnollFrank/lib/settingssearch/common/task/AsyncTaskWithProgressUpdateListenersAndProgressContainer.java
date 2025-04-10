package de.KnollFrank.lib.settingssearch.common.task;

import android.view.View;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class AsyncTaskWithProgressUpdateListenersAndProgressContainer<Params, Result> extends AsyncTaskWithProgressUpdateListeners<Params, Result> {

    private final View progressContainer;
    private final OnUiThreadRunner onUiThreadRunner;

    public AsyncTaskWithProgressUpdateListenersAndProgressContainer(
            final BiFunction<Params, ProgressUpdateListener, Result> doInBackground,
            final Consumer<Result> onPostExecute,
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
    protected void onPostExecute(final Result result) {
        setVisibilityOfProgressContainer(View.GONE);
        super.onPostExecute(result);
    }

    private void setVisibilityOfProgressContainer(final int visible) {
        onUiThreadRunner.runNonBlockingOnUiThread(() -> progressContainer.setVisibility(visible));
    }
}
