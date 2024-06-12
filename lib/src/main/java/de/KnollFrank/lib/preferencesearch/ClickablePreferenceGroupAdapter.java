package de.KnollFrank.lib.preferencesearch;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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

class UIUtils {

    public static void setOnClickListener(final View view, final OnClickListener onClickListener) {
        UIUtils.makeChildViewsNonClickable(view);
        view.setOnClickListener(onClickListener);
    }

    private static void makeChildViewsNonClickable(final View view) {
        setClickableRecursive(view, false);
        view.setClickable(true);
    }

    private static void setClickableRecursive(final View view, boolean clickable) {
        view.setClickable(clickable);
        if (view instanceof final ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setClickableRecursive(viewGroup.getChildAt(i), clickable);
            }
        }
    }
}
