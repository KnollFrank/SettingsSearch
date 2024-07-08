package de.KnollFrank.lib.preferencesearch.results.adapter;

import static de.KnollFrank.lib.preferencesearch.results.adapter.ClickListenerSetter.setOnClickListener;
import static de.KnollFrank.lib.preferencesearch.results.adapter.SearchableInfoView.createSearchableInfoView;
import static de.KnollFrank.lib.preferencesearch.results.adapter.SearchableInfoView.displaySearchableInfo;
import static de.KnollFrank.lib.preferencesearch.results.adapter.SearchableInfoViewAdder.addSearchableInfoView;

import android.view.ViewGroup;

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
        return addSearchableInfoView(
                createSearchableInfoView("", parent.getContext()),
                super.onCreateViewHolder(parent, viewType),
                parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Preference preference = getItem(position);
        displaySearchableInfo(holder, searchableInfoGetter.getSearchableInfo(preference));
        setOnClickListener(
                holder.itemView,
                v -> onPreferenceClickListener.accept(preference));
    }
}
