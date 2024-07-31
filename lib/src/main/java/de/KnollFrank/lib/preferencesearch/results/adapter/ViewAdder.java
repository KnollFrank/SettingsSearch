package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

class ViewAdder {

    public static void addSecondAndThirdViewBelowFirstView(final View firstView,
                                                           final View secondView,
                                                           final View thirdView,
                                                           final Context context) {
        final LinearLayout container = createLinearLayout(context, firstView.getLayoutParams());
        replaceView(firstView, container);
        addView2LinearLayout(firstView, container);
        addView2LinearLayout(secondView, container);
        addView2LinearLayout(thirdView, container);
    }

    public static void addView2LinearLayout(final View view, final LinearLayout linearLayout) {
        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
        linearLayout.addView(view);
    }

    public static LinearLayout createLinearLayout(final Context context, final LayoutParams layoutParams) {
        final LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(layoutParams);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    // taken from https://stackoverflow.com/a/56793843
    private static void replaceView(final View oldView, final View newView) {
        final ViewGroup oldViewParent = (ViewGroup) oldView.getParent();
        final int indexOfOldView = oldViewParent.indexOfChild(oldView);
        oldViewParent.removeViewAt(indexOfOldView);
        oldViewParent.addView(newView, indexOfOldView);
    }
}
