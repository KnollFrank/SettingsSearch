package de.KnollFrank.lib.preferencesearch;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class PreferenceViewHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final TextView summary;
    public final TextView breadcrumbs;

    public PreferenceViewHolder(final View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        summary = itemView.findViewById(R.id.summary);
        breadcrumbs = itemView.findViewById(R.id.breadcrumbs);
    }
}
