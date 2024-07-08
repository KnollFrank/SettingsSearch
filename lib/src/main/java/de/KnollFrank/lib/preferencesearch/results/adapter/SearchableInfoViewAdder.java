package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.preference.PreferenceViewHolder;

import java.util.Optional;

class SearchableInfoViewAdder {

    public static PreferenceViewHolder addSearchableInfoView(final View searchableInfoView,
                                                             final PreferenceViewHolder holder,
                                                             final Context context) {
        final Optional<View> summaryView = findViewById(holder, android.R.id.summary);
        if (summaryView.isPresent()) {
            ViewAdder.addSecondViewBelowFirstView(
                    summaryView.get(),
                    searchableInfoView,
                    context);
            return holder;
        } else {
            final LinearLayout container =
                    createLinearLayoutWithTwoChildren(
                            holder.itemView,
                            searchableInfoView,
                            context);
            return PreferenceViewHolder.createInstanceForTests(container);
        }
    }

    private static LinearLayout createLinearLayoutWithTwoChildren(final View firstChild,
                                                                  final View secondChild,
                                                                  final Context context) {
        final LinearLayout container =
                ViewAdder.createLinearLayout(
                        context,
                        new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
        ViewAdder.addView2LinearLayout(firstChild, container);
        ViewAdder.addView2LinearLayout(secondChild, container);
        return container;
    }

    private static Optional<View> findViewById(final PreferenceViewHolder holder, final @IdRes int id) {
        return Optional.ofNullable(holder.findViewById(id));
    }
}
