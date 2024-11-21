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
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;

public class PreferenceViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    private final Drawable mBackground;
    private ColorStateList mTitleTextColors;
    private final SparseArray<View> mCachedViews = new SparseArray<>(4);
    private boolean mDividerAllowedAbove;
    private boolean mDividerAllowedBelow;

    public PreferenceViewHolder(@NonNull View itemView) {
        super(itemView);

        final TextView titleView = itemView.findViewById(android.R.id.title);

        // Pre-cache the views that we know in advance we'll want to find
        mCachedViews.put(android.R.id.title, titleView);
        mCachedViews.put(android.R.id.summary, itemView.findViewById(android.R.id.summary));
        mCachedViews.put(android.R.id.icon, itemView.findViewById(android.R.id.icon));
        mCachedViews.put(androidx.preference.R.id.icon_frame, itemView.findViewById(androidx.preference.R.id.icon_frame));
        mCachedViews.put(AndroidResources.ANDROID_R_ICON_FRAME,
                itemView.findViewById(AndroidResources.ANDROID_R_ICON_FRAME));

        mBackground = itemView.getBackground();
        if (titleView != null) {
            mTitleTextColors = titleView.getTextColors();
        }
    }

    @NonNull
    public static PreferenceViewHolder createInstanceForTests(@NonNull View itemView) {
        return new PreferenceViewHolder(itemView);
    }

    /**
     * Returns a cached reference to a subview managed by this object. If the view reference is not
     * yet cached, it falls back to calling {@link View#findViewById(int)} and caches the result.
     *
     * @param id Resource ID of the view to find
     * @return The view, or {@code null} if no view with the requested ID is found
     */
    public View findViewById(@IdRes int id) {
        final View cachedView = mCachedViews.get(id);
        if (cachedView != null) {
            return cachedView;
        } else {
            final View v = itemView.findViewById(id);
            if (v != null) {
                mCachedViews.put(id, v);
            }
            return v;
        }
    }

    /**
     * Dividers are only drawn between items if both items allow it, or above the first and below
     * the last item if that item allows it.
     *
     * @return {@code true} if dividers are allowed above this item
     */
    public boolean isDividerAllowedAbove() {
        return mDividerAllowedAbove;
    }

    /**
     * Dividers are only drawn between items if both items allow it, or above the first and below
     * the last item if that item allows it.
     * <p>
     * By default, {@link Preference#onBindViewHolder(PreferenceViewHolder)} will set this to the
     * same value as returned by {@link Preference#isSelectable()}, so that non-selectable items
     * do not have a divider drawn above them.
     *
     * @param allowed False to prevent dividers being drawn above this item
     */
    public void setDividerAllowedAbove(boolean allowed) {
        mDividerAllowedAbove = allowed;
    }

    /**
     * Dividers are only drawn between items if both items allow it, or above the first and below
     * the last item if that item allows it.
     *
     * @return {@code true} if dividers are allowed below this item
     */
    public boolean isDividerAllowedBelow() {
        return mDividerAllowedBelow;
    }

    /**
     * Dividers are only drawn between items if both items allow it, or above the first and below
     * the last item if that item allows it.
     * <p>
     * By default, {@link Preference#onBindViewHolder(PreferenceViewHolder)} will set this to the
     * same value as returned by {@link Preference#isSelectable()}, so that non-selectable items
     * do not have a divider drawn below them.
     *
     * @param allowed False to prevent dividers being drawn below this item
     */
    public void setDividerAllowedBelow(boolean allowed) {
        mDividerAllowedBelow = allowed;
    }

    /**
     * Resets the state of properties modified by
     * {@link Preference#onBindViewHolder(PreferenceViewHolder)} to ensure that we don't keep
     * stale state for a different {@link Preference} around.
     */
    void resetState() {
        if (itemView.getBackground() != mBackground) {
            ViewCompat.setBackground(itemView, mBackground);
        }

        final TextView titleView = (TextView) findViewById(android.R.id.title);
        if (titleView != null && mTitleTextColors != null) {
            if (!titleView.getTextColors().equals(mTitleTextColors)) {
                titleView.setTextColor(mTitleTextColors);
            }
        }
    }
}
