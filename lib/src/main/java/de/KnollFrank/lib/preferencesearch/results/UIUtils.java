package de.KnollFrank.lib.preferencesearch.results;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

class UIUtils {

    public static void setOnClickListener(final View view, final View.OnClickListener onClickListener) {
        makeChildViewsNonClickable(view);
        view.setOnClickListener(onClickListener);
    }

    public static void addSecondViewBelowFirstView(final View firstView,
                                                   final View secondView,
                                                   final Context context) {
        final LinearLayout container = createLinearLayout(context, firstView.getLayoutParams());
        replaceView(firstView, container);
        addView2LinearLayout(firstView, container);
        addView2LinearLayout(secondView, container);
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

    private static LinearLayout createLinearLayout(final Context context, final LayoutParams layoutParams) {
        final LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(layoutParams);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private static void replaceView(final View oldView, final View newView) {
        final ViewGroup oldViewParent = (ViewGroup) oldView.getParent();
        final int indexOfOldView = oldViewParent.indexOfChild(oldView);
        oldViewParent.removeViewAt(indexOfOldView);
        oldViewParent.addView(newView, indexOfOldView);
    }

    private static void addView2LinearLayout(final View view, final LinearLayout linearLayout) {
        view.setLayoutParams(createLinearLayoutParams());
        linearLayout.addView(view);
    }

    private static LinearLayout.LayoutParams createLinearLayoutParams() {
        return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
