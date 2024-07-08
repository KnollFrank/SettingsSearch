package de.KnollFrank.lib.preferencesearch.results.adapter;

import static de.KnollFrank.lib.preferencesearch.results.adapter.UIUtils.findViewById;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import androidx.preference.PreferenceViewHolder;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Options;

class SearchableInfoViewAdder {

    public static PreferenceViewHolder addSearchableInfoView(final View searchableInfoView,
                                                             final PreferenceViewHolder holder,
                                                             final Context context) {
        if (hasSummaryViewOrTitleView(holder)) {
            ViewAdder.addSecondViewBelowFirstView(
                    getSummaryViewOrTitleView(holder).get(),
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

    private static boolean hasSummaryViewOrTitleView(final PreferenceViewHolder holder) {
        return getSummaryViewOrTitleView(holder).isPresent();
    }

    private static Optional<View> getSummaryViewOrTitleView(final PreferenceViewHolder holder) {
        return Options.or(
                findViewById(holder, android.R.id.summary),
                () -> findViewById(holder, android.R.id.title));
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
}
