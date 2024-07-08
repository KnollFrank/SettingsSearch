package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.preference.PreferenceViewHolder;

import java.util.Optional;

class UIUtils {

    public static Optional<View> findViewById(final PreferenceViewHolder holder, final @IdRes int id) {
        return Optional.ofNullable(holder.findViewById(id));
    }
}
