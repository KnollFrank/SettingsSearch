package de.KnollFrank.lib.preferencesearch.results;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoGetter;

class SearchablePreferenceGroupAdapter extends PreferenceGroupAdapter {

    public static final int SEARCHABLE_INFO_VIEW_ID = View.generateViewId();

    private final SearchableInfoGetter searchableInfoGetter;
    private final Consumer<Preference> onPreferenceClickListener;

    public SearchablePreferenceGroupAdapter(final PreferenceGroup preferenceGroup,
                                            final SearchableInfoGetter searchableInfoGetter,
                                            final Consumer<Preference> onPreferenceClickListener) {
        super(preferenceGroup);
        this.searchableInfoGetter = searchableInfoGetter;
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        // FK-TODO: replace summary TextView with a LinearLayout containing the original summary TextView and the SearchableInfoView
        final LinearLayout container = createLinearLayout(parent.getContext());
        container.addView(super.onCreateViewHolder(parent, viewType).itemView);
        container.addView(createSearchableInfoView("", parent.getContext()));
        return PreferenceViewHolder.createInstanceForTests(container);
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Preference preference = getItem(position);
        displaySearchableInfo(holder, preference);
        UIUtils.setOnClickListener(
                holder.itemView,
                v -> onPreferenceClickListener.accept(preference));
    }

    // FK-TODO: move UI code to new class
    private static LinearLayout createLinearLayout(final Context context) {
        final LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private static TextView createSearchableInfoView(final String text, final Context context) {
        final TextView searchableInfoView = new TextView(context);
        searchableInfoView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
        searchableInfoView.setText(text);
        searchableInfoView.setId(SEARCHABLE_INFO_VIEW_ID);
        return searchableInfoView;
    }

    private static TextView getSearchableInfoView(final PreferenceViewHolder holder) {
        return (TextView) holder.findViewById(SEARCHABLE_INFO_VIEW_ID);
    }

    private void displaySearchableInfo(final PreferenceViewHolder holder, final Preference preference) {
        final TextView searchableInfoView = getSearchableInfoView(holder);
        final CharSequence searchableInfo =
                searchableInfoGetter
                        .getSearchableInfo(preference)
                        .orElse(null);
        searchableInfoView.setText(searchableInfo);
    }
}
