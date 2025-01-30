package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.preference.AndroidResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Optional;

// adapted from androidx.preference.PreferenceViewHolder
public class PreferenceViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    private final Drawable background;
    private final ColorStateList titleTextColors;
    private final SparseArray<View> cachedViews = new SparseArray<>(4);

    public PreferenceViewHolder(@NonNull View itemView) {
        super(itemView);
        final TextView titleView = itemView.findViewById(android.R.id.title);
        // Pre-cache the views that we know in advance we'll want to find
        cachedViews.put(android.R.id.title, titleView);
        cachedViews.put(android.R.id.summary, itemView.findViewById(android.R.id.summary));
        cachedViews.put(android.R.id.icon, itemView.findViewById(android.R.id.icon));
        cachedViews.put(androidx.preference.R.id.icon_frame, itemView.findViewById(androidx.preference.R.id.icon_frame));
        cachedViews.put(AndroidResources.ANDROID_R_ICON_FRAME, itemView.findViewById(AndroidResources.ANDROID_R_ICON_FRAME));
        background = itemView.getBackground();
        titleTextColors = titleView != null ? titleView.getTextColors() : null;
    }

    public <T extends View> Optional<T> findViewById(@IdRes int id) {
        final T cachedView = (T) cachedViews.get(id);
        if (cachedView != null) {
            return Optional.of(cachedView);
        } else {
            final T view = itemView.findViewById(id);
            if (view != null) {
                cachedViews.put(id, view);
            }
            return Optional.ofNullable(view);
        }
    }

    void resetState() {
        if (itemView.getBackground() != background) {
            ViewCompat.setBackground(itemView, background);
        }
        if (titleTextColors != null) {
            this
                    .<TextView>findViewById(android.R.id.title)
                    .ifPresent(titleView -> setTextColor(titleView, titleTextColors));
        }
    }

    private static void setTextColor(final TextView titleView, final ColorStateList colorStateList) {
        if (!titleView.getTextColors().equals(colorStateList)) {
            titleView.setTextColor(colorStateList);
        }
    }
}
