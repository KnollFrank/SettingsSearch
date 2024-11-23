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

// FK-TODO: refactor
public class PreferenceViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    private final Drawable mBackground;
    private ColorStateList mTitleTextColors;
    private final SparseArray<View> mCachedViews = new SparseArray<>(4);

    public PreferenceViewHolder(@NonNull View itemView) {
        super(itemView);
        final TextView titleView = itemView.findViewById(android.R.id.title);
        // Pre-cache the views that we know in advance we'll want to find
        mCachedViews.put(android.R.id.title, titleView);
        mCachedViews.put(android.R.id.summary, itemView.findViewById(android.R.id.summary));
        mCachedViews.put(android.R.id.icon, itemView.findViewById(android.R.id.icon));
        mCachedViews.put(androidx.preference.R.id.icon_frame, itemView.findViewById(androidx.preference.R.id.icon_frame));
        mCachedViews.put(AndroidResources.ANDROID_R_ICON_FRAME, itemView.findViewById(AndroidResources.ANDROID_R_ICON_FRAME));
        mBackground = itemView.getBackground();
        if (titleView != null) {
            mTitleTextColors = titleView.getTextColors();
        }
    }

    public static PreferenceViewHolder createInstance(final View itemView) {
        return new PreferenceViewHolder(itemView);
    }

    public <T extends View> Optional<T> findViewById(@IdRes int id) {
        final T cachedView = (T) mCachedViews.get(id);
        if (cachedView != null) {
            return Optional.of(cachedView);
        } else {
            final T view = itemView.findViewById(id);
            if (view != null) {
                mCachedViews.put(id, view);
            }
            return Optional.ofNullable(view);
        }
    }

    void resetState() {
        if (itemView.getBackground() != mBackground) {
            ViewCompat.setBackground(itemView, mBackground);
        }
        if (mTitleTextColors != null) {
            this
                    .<TextView>findViewById(android.R.id.title)
                    .ifPresent(titleView -> setTextColor(titleView, mTitleTextColors));
        }
    }

    private static void setTextColor(final TextView titleView, final ColorStateList colorStateList) {
        if (!titleView.getTextColors().equals(colorStateList)) {
            titleView.setTextColor(colorStateList);
        }
    }
}
