package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;
import android.widget.TextView;

public class ProgressDisplayer implements ProgressUpdateListener {

    private final View progressContainer;
    private final TextView progressText;

    protected ProgressDisplayer(final View progressContainer, final TextView progressText) {
        this.progressContainer = progressContainer;
        this.progressText = progressText;
    }

    @Override
    public void onProgressUpdate(final String progress) {
        progressContainer.setVisibility(View.VISIBLE);
        progressText.setText(progress);
    }
}
