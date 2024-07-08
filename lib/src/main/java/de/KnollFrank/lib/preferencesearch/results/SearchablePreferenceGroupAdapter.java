package de.KnollFrank.lib.preferencesearch.results;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoGetter;

class SearchablePreferenceGroupAdapter extends PreferenceGroupAdapter {

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
        createSearchableInfoViewBelowSummary(preferenceViewHolder, parent.getContext());
        return preferenceViewHolder;
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

    private static void createSearchableInfoViewBelowSummary(final PreferenceViewHolder preferenceViewHolder, final Context context) {
        ViewAdder.addSecondViewBelowFirstView(
                preferenceViewHolder.findViewById(android.R.id.summary),
                SearchableInfoView.createSearchableInfoView("", context),
                context);
    }
}
