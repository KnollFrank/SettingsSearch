package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.R;

class SearchableInfoView {

    private static final int SEARCHABLE_INFO_VIEW_ID = R.id.searchable_info;

    public static TextView createSearchableInfoView(final String text, final Context context) {
        final TextView searchableInfoView = new TextView(context);
        searchableInfoView.setText(text);
        searchableInfoView.setId(SEARCHABLE_INFO_VIEW_ID);
        searchableInfoView.setVisibility(View.GONE);
        return searchableInfoView;
    }

    public static Optional<TextView> getSearchableInfoView(final PreferenceViewHolder holder) {
        return holder.findViewById(SEARCHABLE_INFO_VIEW_ID);
    }
}
