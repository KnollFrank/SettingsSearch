package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {

    private final List<SearchablePreferencePOJO> data = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public SearchResultsRecyclerViewAdapter(final Context context) {
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
                        .orElse("some unknown title"));
        holder.summary.setText(
                searchablePreferencePOJO
                        .summary()
                        .orElse("some unknown summary"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView summary;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            summary = itemView.findViewById(R.id.summary);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        }
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
