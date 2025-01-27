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
        holder.contentView.setText(items.get(position).content());
        holder.detailsView.setText(items.get(position).details());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView idView;
        public final TextView contentView;
        public final TextView detailsView;
        public PlaceholderItem item;

        public ViewHolder(final FragmentItemBinding binding) {
            super(binding.getRoot());
            idView = binding.itemNumber;
            contentView = binding.content;
            detailsView = binding.details;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}