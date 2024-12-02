package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;
import android.widget.TextView;

import java.util.function.Function;

public class ProgressDisplayerFactory {

    public static ProgressDisplayer createProgressDisplayer(final View progressContainer,
                                                            final Function<View, TextView> getProgressText) {
        return new ProgressDisplayer(
                progressContainer,
                getProgressText.apply(progressContainer));
    }
}
