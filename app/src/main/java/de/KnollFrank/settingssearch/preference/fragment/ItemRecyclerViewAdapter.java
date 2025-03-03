package de.KnollFrank.settingssearch.preference.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.KnollFrank.settingssearch.databinding.FragmentItemBinding;
import de.KnollFrank.settingssearch.preference.fragment.placeholder.PlaceholderContent.PlaceholderItem;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> items;

    public ItemRecyclerViewAdapter(final List<PlaceholderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PlaceholderItem item = items.get(position);
        holder.item = item;
        holder.keyView.setText(item.key());
        holder.titleView.setText(item.title());
        holder.summaryView.setText(item.summary());
        item.onClickListener().ifPresent(holder.itemView::setOnClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView keyView;
        public final TextView titleView;
        public final TextView summaryView;
        public PlaceholderItem item;

        public ViewHolder(final FragmentItemBinding binding) {
            super(binding.getRoot());
            keyView = binding.key;
            titleView = binding.title;
            summaryView = binding.summary;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}