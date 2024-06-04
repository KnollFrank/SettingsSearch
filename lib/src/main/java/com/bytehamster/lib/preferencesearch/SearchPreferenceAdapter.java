package com.bytehamster.lib.preferencesearch;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;

class SearchPreferenceAdapter extends RecyclerView.Adapter<SearchPreferenceAdapter.ViewHolder> {

    private ImmutableList<ListItem> dataset = ImmutableList.of();
    private SearchConfiguration searchConfiguration;
    private SearchClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == PreferenceItem.TYPE) {
            return new PreferenceViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.searchpreference_list_item_result, parent, false));
        } else {
            return new HistoryViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.searchpreference_list_item_history, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final ListItem listItem = dataset.get(position);
        if (getItemViewType(position) == HistoryItem.TYPE) {
            final HistoryViewHolder historyViewHolder = (HistoryViewHolder) viewHolder;
            HistoryItem item = (HistoryItem) listItem;
            historyViewHolder.term.setText(item.getTerm());
        } else if (getItemViewType(position) == PreferenceItem.TYPE) {
            final PreferenceViewHolder preferenceViewHolder = (PreferenceViewHolder) viewHolder;
            final PreferenceItem item = (PreferenceItem) listItem;
            preferenceViewHolder.title.setText(item.title);

            if (TextUtils.isEmpty(item.summary)) {
                preferenceViewHolder.summary.setVisibility(View.GONE);
            } else {
                preferenceViewHolder.summary.setVisibility(View.VISIBLE);
                preferenceViewHolder.summary.setText(item.summary);
            }

            if (searchConfiguration.isBreadcrumbsEnabled()) {
                preferenceViewHolder.breadcrumbs.setText(item.breadcrumbs);
                preferenceViewHolder.breadcrumbs.setAlpha(0.6f);
                preferenceViewHolder.summary.setAlpha(1.0f);
            } else {
                preferenceViewHolder.breadcrumbs.setVisibility(View.GONE);
                preferenceViewHolder.summary.setAlpha(0.6f);
            }
        }

        viewHolder.root.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(listItem, viewHolder.getAdapterPosition());
            }
        });
    }

    void setContent(final ImmutableList<ListItem> items) {
        dataset = items;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataset.get(position).getType();
    }

    void setSearchConfiguration(SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    void setOnItemClickListener(SearchClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    interface SearchClickListener {

        void onItemClicked(ListItem item, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final View root;

        public ViewHolder(final View root) {
            super(root);
            this.root = root;
        }
    }

    static class HistoryViewHolder extends ViewHolder {

        final TextView term;

        HistoryViewHolder(View v) {
            super(v);
            term = v.findViewById(R.id.term);
        }
    }

    static class PreferenceViewHolder extends ViewHolder {

        public final TextView title;
        public final TextView summary;
        public final TextView breadcrumbs;

        public PreferenceViewHolder(final View view) {
            super(view);
            title = view.findViewById(R.id.title);
            summary = view.findViewById(R.id.summary);
            breadcrumbs = view.findViewById(R.id.breadcrumbs);
        }
    }
}
