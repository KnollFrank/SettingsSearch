package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.preference.PreferenceViewHolder;

import java.util.Optional;

class ViewsAdder {

    public static PreferenceViewHolder addSearchableInfoViewAndPreferencePathView(
            final View searchableInfoView,
            final View preferencePathView,
            final PreferenceViewHolder holder,
            final Context context) {
        final Optional<View> summaryView = findViewById(holder, android.R.id.summary);
        if (summaryView.isPresent()) {
            ViewAdder.addSecondAndThirdViewBelowFirstView(
                    summaryView.get(),
                    searchableInfoView,
                    preferencePathView,
                    context);
            return holder;
        } else {
            final LinearLayout container =
                    createLinearLayoutWithThreeChildren(
                            holder.itemView,
                            searchableInfoView,
                            preferencePathView,
                            context);
            return PreferenceViewHolder.createInstanceForTests(container);
        }
    }

    // FK-TODO: replace the three Views with a List<View>
    private static LinearLayout createLinearLayoutWithThreeChildren(final View firstChild,
                                                                    final View secondChild,
                                                                    final View thirdChild,
                                                                    final Context context) {
        final LinearLayout container =
                ViewAdder.createLinearLayout(
                        context,
                        new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
        ViewAdder.addView2LinearLayout(firstChild, container);
        ViewAdder.addView2LinearLayout(secondChild, container);
        ViewAdder.addView2LinearLayout(thirdChild, container);
        return container;
    }

    private static Optional<View> findViewById(final PreferenceViewHolder holder, final @IdRes int id) {
        return Optional.ofNullable(holder.findViewById(id));
    }
}
