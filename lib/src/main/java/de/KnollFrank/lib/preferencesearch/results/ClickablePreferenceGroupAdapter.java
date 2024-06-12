package de.KnollFrank.lib.preferencesearch.results;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.util.function.Consumer;

class ClickablePreferenceGroupAdapter extends PreferenceGroupAdapter {

    private final Consumer<Preference> onPreferenceClickListener;

    public ClickablePreferenceGroupAdapter(final PreferenceGroup preferenceGroup,
                                           final Consumer<Preference> onPreferenceClickListener) {
        super(preferenceGroup);
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        UIUtils.setOnClickListener(
                holder.itemView,
                v -> onPreferenceClickListener.accept(getItem(position)));
    }
}
