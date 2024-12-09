package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.R;

class PreferencePathView {

    private static final int PREFERENCE_PATH_VIEW_ID = R.id.preference_path;

    public static TextView createPreferencePathView(final Context context) {
        final TextView preferencePathView = new TextView(context);
        preferencePathView.setId(PREFERENCE_PATH_VIEW_ID);
        preferencePathView.setVisibility(View.GONE);
        return preferencePathView;
    }

    public static void displayPreferencePath(final Optional<TextView> preferencePathView,
                                             final PreferencePath preferencePath,
                                             final boolean showPreferencePath,
                                             final PreferencePathDisplayer preferencePathDisplayer) {
        if (showPreferencePath) {
            TextViews.setTextOnOptionalTextView(
                    preferencePathView,
                    TextUtils.concat(
                            "Path: ",
                            preferencePathDisplayer.display(preferencePath)));
        } else {
            preferencePathView.ifPresent(_preferencePathView -> _preferencePathView.setVisibility(View.GONE));
        }
    }

    public static Optional<TextView> getPreferencePathView(final PreferenceViewHolder holder) {
        return holder.findViewById(PREFERENCE_PATH_VIEW_ID);
    }
}
