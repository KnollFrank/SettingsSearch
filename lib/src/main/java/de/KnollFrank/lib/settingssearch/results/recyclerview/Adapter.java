package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<SearchablePreferencePOJO> data = new ArrayList<>();
    private final Consumer<SearchablePreferencePOJO> onPreferenceClickListener;
    private final LayoutInflater layoutInflater;

    public Adapter(final Consumer<SearchablePreferencePOJO> onPreferenceClickListener, final Context context) {
        this.onPreferenceClickListener = onPreferenceClickListener;
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
        final SearchablePreferencePOJO searchablePreferencePOJO = getItem(position);
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
        holder.itemView.setOnClickListener(view -> onPreferenceClickListener.accept(searchablePreferencePOJO));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public SearchablePreferencePOJO getItem(final int position) {
        return data.get(position);
    }

    public void setData(final List<SearchablePreferencePOJO> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}
