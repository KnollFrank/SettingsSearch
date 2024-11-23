package de.KnollFrank.lib.settingssearch.results.recyclerview;

import static de.KnollFrank.lib.settingssearch.results.recyclerview.TextViews.setOptionalTextOnOptionalTextView;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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

    public static void displayPreferencePath(final Optional<TextView> preferencePathView,
                                             final Optional<PreferencePath> preferencePath,
                                             final boolean showPreferencePath) {
        if (showPreferencePath) {
            setOptionalTextOnOptionalTextView(
                    preferencePathView,
                    preferencePath.map(
                            _preferencePath ->
                                    MessageFormat.format(
                                            "Path: {0}",
                                            toString(_preferencePath))));
        } else {
            preferencePathView.ifPresent(_preferencePathView -> _preferencePathView.setVisibility(View.GONE));
        }
    }

    public static Optional<TextView> getPreferencePathView(final PreferenceViewHolder holder) {
        return holder.findViewById(PREFERENCE_PATH_VIEW_ID);
    }

    private static String toString(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(searchablePreferencePOJO ->
                        searchablePreferencePOJO
                                .getTitle()
                                .orElse("?"))
                .collect(Collectors.joining(" > "));
    }
}
