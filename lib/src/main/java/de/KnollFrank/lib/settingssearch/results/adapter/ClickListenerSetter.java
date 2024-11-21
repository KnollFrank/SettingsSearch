package de.KnollFrank.lib.settingssearch.results.adapter;

import android.view.View;
import android.view.ViewGroup;

public class ClickListenerSetter {

    public static void setOnClickListener(final View view, final View.OnClickListener onClickListener) {
        disableClicksOnSubviews(view);
        view.setOnClickListener(onClickListener);
    }

    public static void disableClicksOnSubviews(final View view) {
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
