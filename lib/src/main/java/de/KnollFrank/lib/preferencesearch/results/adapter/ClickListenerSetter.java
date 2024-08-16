package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.Optional;

class ClickListenerSetter {

    public static void setOnClickListener(final View view, final Optional<View.OnClickListener> onClickListener) {
        setClickableRecursive(view, false);
        onClickListener.ifPresent(
                _onClickListener -> {
                    view.setClickable(true);
                    view.setOnClickListener(_onClickListener);
                });
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
