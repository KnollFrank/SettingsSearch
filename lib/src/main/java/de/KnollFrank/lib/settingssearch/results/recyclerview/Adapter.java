package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<SearchablePreferencePOJO> data = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public Adapter(final Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SearchablePreferencePOJO searchablePreferencePOJO = data.get(position);
        holder.title.setText(
                searchablePreferencePOJO
                        .title()
                        .orElse("unknown title"));
        holder.summary.setText(
                searchablePreferencePOJO
                        .summary()
                        .orElse("unknown summary"));
        holder.searchableInfo.setText(
                searchablePreferencePOJO
                        .searchableInfo()
                        .orElse("unknown searchable info"));
        holder.itemView.setOnClickListener(
                view -> {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, position);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public SearchablePreferencePOJO getItem(final int id) {
        return data.get(id);
    }

    public void setItemClickListener(final ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(final List<SearchablePreferencePOJO> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
