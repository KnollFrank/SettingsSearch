package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;
import android.widget.TextView;

import de.KnollFrank.lib.settingssearch.R;

public class ProgressDisplayer {

    private final View progressContainer;

    public ProgressDisplayer(final View progressContainer) {
        this.progressContainer = progressContainer;
    }

    public void displayProgress(final String progress) {
        progressContainer
                .<TextView>findViewById(R.id.progressText)
                .setText(progress);
    }
}
