package de.KnollFrank.lib.preferencesearch.results;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.preference.PreferenceViewHolder;

import java.util.Optional;

class SearchableInfoView {

    private static final int SEARCHABLE_INFO_VIEW_ID = View.generateViewId();

    public static TextView createSearchableInfoView(final String text, final Context context) {
        final TextView searchableInfoView = new TextView(context);
        searchableInfoView.setText(text);
        searchableInfoView.setId(SEARCHABLE_INFO_VIEW_ID);
        searchableInfoView.setVisibility(View.GONE);
        return searchableInfoView;
    }

    public static void displaySearchableInfo(final PreferenceViewHolder holder,
                                             final Optional<CharSequence> searchableInfo) {
        final TextView searchableInfoView = getSearchableInfoView(holder);
        if (searchableInfo.isPresent()) {
            searchableInfoView.setText(searchableInfo.get());
            searchableInfoView.setVisibility(View.VISIBLE);
        } else {
            searchableInfoView.setVisibility(View.GONE);
        }
    }

    private static TextView getSearchableInfoView(final PreferenceViewHolder holder) {
        return (TextView) holder.findViewById(SEARCHABLE_INFO_VIEW_ID);
    }
}
