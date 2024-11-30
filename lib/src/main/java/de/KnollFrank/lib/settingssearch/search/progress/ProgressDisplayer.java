package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;
import android.widget.TextView;

import de.KnollFrank.lib.settingssearch.R;

class ProgressDisplayer implements ProgressUpdateListener {

    private final View progressContainer;

    public ProgressDisplayer(final View progressContainer) {
        this.progressContainer = progressContainer;
    }

    @Override
    public void onProgressUpdate(final String progress) {
        progressContainer.setVisibility(View.VISIBLE);
        progressContainer
                .<TextView>findViewById(R.id.progressText)
                .setText(progress);
    }
}
