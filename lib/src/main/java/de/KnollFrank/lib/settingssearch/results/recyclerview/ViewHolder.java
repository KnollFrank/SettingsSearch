package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.KnollFrank.lib.settingssearch.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final TextView summary;
    public final TextView searchableInfo;
    public final ImageView icon;

    public ViewHolder(final View itemView) {
        super(itemView);
        title = itemView.findViewById(android.R.id.title);
        summary = itemView.findViewById(android.R.id.summary);
        searchableInfo = itemView.findViewById(R.id.searchable_info);
        icon = itemView.findViewById(android.R.id.icon);
    }
}
