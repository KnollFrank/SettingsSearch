package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import java.util.List;

class ViewAdder {

    private ViewAdder() {
    }

    public static void replaceViewWithViews(final View viewToReplace,
                                            final List<View> replacingViews,
                                            final Context context) {
        final LinearLayout container = createLinearLayout(context, viewToReplace.getLayoutParams());
        replaceView(viewToReplace, container);
        addViewsToLinearLayout(replacingViews, container);
    }

    public static void addViewsToLinearLayout(final List<View> children, final LinearLayout linearLayout) {
        for (final View child : children) {
            addViewToLinearLayout(child, linearLayout);
        }
    }

    public static void addViewToLinearLayout(final View view, final LinearLayout linearLayout) {
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
