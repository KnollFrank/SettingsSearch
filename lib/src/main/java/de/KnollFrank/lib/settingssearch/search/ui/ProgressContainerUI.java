package de.KnollFrank.lib.settingssearch.search.ui;

import android.view.View;
import android.widget.TextView;

public interface ProgressContainerUI {

    View getProgressContainer(View rootView);

    TextView getProgressText(View progressContainer);
}
