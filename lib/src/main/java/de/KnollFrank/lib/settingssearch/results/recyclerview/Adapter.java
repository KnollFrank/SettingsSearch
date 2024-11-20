package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

// FK-TODO: see androidx.preference.PreferenceGroupAdapter
public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<SearchablePreferencePOJO> data = new ArrayList<>();
    private final Consumer<SearchablePreferencePOJO> onPreferenceClickListener;

    public Adapter(final Consumer<SearchablePreferencePOJO> onPreferenceClickListener) {
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    // FK-TODO: adapt from PreferenceGroupAdapter.onCreateViewHolder()
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.preference_material, parent, false);
        return new ViewHolder(view);
    }

    // FK-TODO: adapt from PreferenceGroupAdapter.onBindViewHolder()
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
        Adapter
                .getIcon(searchablePreferencePOJO, holder.itemView.getContext())
                .ifPresent(holder.icon::setImageDrawable);
        holder.itemView.setOnClickListener(view -> onPreferenceClickListener.accept(searchablePreferencePOJO));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(final List<SearchablePreferencePOJO> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    private SearchablePreferencePOJO getItem(final int position) {
        return data.get(position);
    }

    private static Optional<Drawable> getIcon(final SearchablePreferencePOJO searchablePreferencePOJO,
                                              final Context context) {
        return searchablePreferencePOJO
                .iconResourceIdOrIconPixelData()
                .map(iconResourceIdOrIconPixelData ->
                        iconResourceIdOrIconPixelData.join(
                                iconResourceId -> AppCompatResources.getDrawable(context, iconResourceId),
                                iconPixelData ->
                                        DrawableAndStringConverter.string2Drawable(
                                                iconPixelData,
                                                context.getResources())));
    }
}
