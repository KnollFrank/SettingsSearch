package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.preference.PreferenceViewHolder;

import de.KnollFrank.lib.preferencesearch.PreferencePath;

class PreferencePathView {

    private static final int PREFERENCE_PATH_VIEW_ID = View.generateViewId();

    public static TextView createPreferencePathView(final Context context) {
        final TextView preferencePathView = new TextView(context);
        preferencePathView.setId(PREFERENCE_PATH_VIEW_ID);
        preferencePathView.setVisibility(View.GONE);
        return preferencePathView;
    }

    public static void displayPreferencePath(final PreferenceViewHolder holder,
                                             final PreferencePath preferencePath) {
        final TextView preferencePathView = getPreferencePathView(holder);
        preferencePathView.setText(preferencePath.toString());
        preferencePathView.setVisibility(View.VISIBLE);
    }

    private static TextView getPreferencePathView(final PreferenceViewHolder holder) {
        return (TextView) holder.findViewById(PREFERENCE_PATH_VIEW_ID);
    }
}
