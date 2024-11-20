package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.KnollFrank.lib.settingssearch.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public final TextView summary;

    public ViewHolder(final View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        summary = itemView.findViewById(R.id.summary);
    }
}
