package de.KnollFrank.lib.settingssearch.results.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static void displayPreferencePath(final PreferenceViewHolder holder,
                                             final Optional<PreferencePath> preferencePath,
                                             final boolean showPreferencePath) {
        final TextView preferencePathView = getPreferencePathView(holder);
        if (showPreferencePath && preferencePath.isPresent()) {
            preferencePathView.setText(
                    MessageFormat.format(
                            "Path: {0}",
                            toString(preferencePath.get())));
            preferencePathView.setVisibility(View.VISIBLE);
        } else {
            preferencePathView.setVisibility(View.GONE);
        }
    }

    public static boolean hasPreferencePathView(final PreferenceViewHolder holder) {
        return holder.findViewById(PREFERENCE_PATH_VIEW_ID) != null;
    }

    private static TextView getPreferencePathView(final PreferenceViewHolder holder) {
        return (TextView) holder.findViewById(PREFERENCE_PATH_VIEW_ID);
    }

    private static String toString(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(Preference::getTitle)
                .collect(Collectors.joining(" > "));
    }
}
