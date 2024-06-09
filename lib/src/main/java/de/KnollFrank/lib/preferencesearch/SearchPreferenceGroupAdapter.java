package de.KnollFrank.lib.preferencesearch;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.util.function.Consumer;
import java.util.function.Function;

class SearchPreferenceGroupAdapter extends PreferenceGroupAdapter {

    private final Consumer<PreferenceWithHost> onPreferenceClickListener;
    private final Function<Preference, PreferenceWithHost> getPreferenceWithHost;

    public SearchPreferenceGroupAdapter(
            final PreferenceGroup preferenceGroup,
            final Consumer<PreferenceWithHost> onPreferenceClickListener,
            final Function<Preference, PreferenceWithHost> getPreferenceWithHost) {
        super(preferenceGroup);
        this.onPreferenceClickListener = onPreferenceClickListener;
        this.getPreferenceWithHost = getPreferenceWithHost;
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(
                v -> onPreferenceClickListener.accept(getPreferenceWithHost(position)));
    }

    private PreferenceWithHost getPreferenceWithHost(final int position) {
        return this.getPreferenceWithHost.apply(getItem(position));
    }
}
