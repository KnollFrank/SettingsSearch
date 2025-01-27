package de.KnollFrank.settingssearch.preference.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.KnollFrank.settingssearch.databinding.FragmentItemBinding;
import de.KnollFrank.settingssearch.preference.fragment.placeholder.PlaceholderContent.PlaceholderItem;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> items;

    public ItemRecyclerViewAdapter(final List<PlaceholderItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item = items.get(position);
        holder.idView.setText(items.get(position).id());
        holder.titleView.setText(items.get(position).title());
        holder.summaryView.setText(items.get(position).summary());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView idView;
        public final TextView titleView;
        public final TextView summaryView;
        public PlaceholderItem item;

        public ViewHolder(final FragmentItemBinding binding) {
            super(binding.getRoot());
            idView = binding.itemNumber;
            titleView = binding.title;
            summaryView = binding.summary;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}