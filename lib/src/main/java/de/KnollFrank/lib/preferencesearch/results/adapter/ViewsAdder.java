package de.KnollFrank.lib.preferencesearch.results.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.preference.PreferenceViewHolder;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

class ViewsAdder {

    public static PreferenceViewHolder addSearchableInfoViewAndPreferencePathView(
            final View searchableInfoView,
            final View preferencePathView,
            final PreferenceViewHolder holder,
            final Context context) {
        final Optional<View> summaryView = findViewById(holder, android.R.id.summary);
        if (summaryView.isPresent()) {
            ViewAdder.replaceViewWithViews(
                    summaryView.get(),
                    ImmutableList.of(
                            summaryView.get(),
                            searchableInfoView,
                            preferencePathView),
                    context);
            return holder;
        } else {
            final LinearLayout container =
                    createLinearLayoutWithChildren(
                            ImmutableList.of(
                                    holder.itemView,
                                    searchableInfoView,
                                    preferencePathView),
                            context);
            return PreferenceViewHolder.createInstanceForTests(container);
        }
    }

    private static LinearLayout createLinearLayoutWithChildren(final List<View> children,
                                                               final Context context) {
        final LinearLayout container =
                ViewAdder.createLinearLayout(
                        context,
                        new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
        ViewAdder.addViews2LinearLayout(children, container);
        return container;
    }

    private static Optional<View> findViewById(final PreferenceViewHolder holder, final @IdRes int id) {
        return Optional.ofNullable(holder.findViewById(id));
    }
}
