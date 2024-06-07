package de.KnollFrank.lib.preferencesearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;

class SearchPreferenceAdapter extends RecyclerView.Adapter<PreferenceViewHolder> {

    private ImmutableList<PreferenceItem> preferenceItems = ImmutableList.of();
    private SearchConfiguration searchConfiguration;
    private SearchClickListener onItemClickListener;

    public void setSearchConfiguration(final SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public void setOnItemClickListener(final SearchClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new PreferenceViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.searchpreference_list_item_result,
                                parent,
                                false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder preferenceViewHolder,
                                 final int position) {
        final PreferenceItem preferenceItem = preferenceItems.get(position);
        preferenceViewHolder.title.setText(preferenceItem.title.orElse(null));

        preferenceViewHolder.summary.setText(preferenceItem.summary.orElse(null));
        preferenceViewHolder.summary.setVisibility(preferenceItem.summary.isPresent() ? View.VISIBLE : View.GONE);

        if (searchConfiguration.isBreadcrumbsEnabled()) {
            preferenceViewHolder.breadcrumbs.setText(preferenceItem.breadcrumbs.orElse(null));
            preferenceViewHolder.breadcrumbs.setAlpha(0.6f);
            preferenceViewHolder.summary.setAlpha(1.0f);
        } else {
            preferenceViewHolder.breadcrumbs.setVisibility(View.GONE);
            preferenceViewHolder.summary.setAlpha(0.6f);
        }

        preferenceViewHolder.itemView.setOnClickListener(
                view -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(preferenceItem, preferenceViewHolder.getAdapterPosition());
                    }
                });
    }

    public void setPreferenceItems(final ImmutableList<PreferenceItem> preferenceItems) {
        this.preferenceItems = preferenceItems;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return preferenceItems.size();
    }
}
