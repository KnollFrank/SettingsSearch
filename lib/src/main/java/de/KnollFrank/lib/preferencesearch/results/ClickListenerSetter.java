package de.KnollFrank.lib.preferencesearch.results;

import android.view.View;
import android.view.ViewGroup;

class ClickListenerSetter {

    public static void setOnClickListener(final View view, final View.OnClickListener onClickListener) {
        makeChildViewsNonClickable(view);
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
