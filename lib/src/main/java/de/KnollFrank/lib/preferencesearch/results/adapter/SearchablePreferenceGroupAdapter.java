package de.KnollFrank.lib.preferencesearch.results.adapter;

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

public class SearchablePreferenceGroupAdapter extends PreferenceGroupAdapter {

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
        final PreferenceViewHolder preferenceViewHolder = super.onCreateViewHolder(parent, viewType);
        final Context context = parent.getContext();
        // FK-TODO: add test for Preference without title
        // FK-TODO: add test for Preference without summary
        // FK-TODO: refactor
        final View summaryViewOrTitleView = getSummaryViewOrTitleView(preferenceViewHolder);
        final TextView searchableInfoView = SearchableInfoView.createSearchableInfoView("", context);
        if (summaryViewOrTitleView != null) {
            ViewAdder.addSecondViewBelowFirstView(summaryViewOrTitleView, searchableInfoView, context);
            return preferenceViewHolder;
        } else {
            final LinearLayout container = createLinearLayout(context);
            container.addView(preferenceViewHolder.itemView);
            container.addView(searchableInfoView);
            return PreferenceViewHolder.createInstanceForTests(container);
        }
    }

    private static LinearLayout createLinearLayout(final Context context) {
        return ViewAdder.createLinearLayout(
                context,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Preference preference = getItem(position);
        SearchableInfoView.displaySearchableInfo(holder, searchableInfoGetter.getSearchableInfo(preference));
        ClickListenerSetter.setOnClickListener(
                holder.itemView,
                v -> onPreferenceClickListener.accept(preference));
    }

    private static View getSummaryViewOrTitleView(final PreferenceViewHolder holder) {
        final View summaryView = getSummaryView(holder);
        return summaryView != null ? summaryView : getTitleView(holder);
    }

    private static View getSummaryView(final PreferenceViewHolder holder) {
        return holder.findViewById(android.R.id.summary);
    }

    private static View getTitleView(final PreferenceViewHolder holder) {
        return holder.findViewById(android.R.id.title);
    }
}
