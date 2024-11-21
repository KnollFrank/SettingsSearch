package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

class ViewsAdder {

    public static PreferenceViewHolder addViews(
            final List<View> views,
            final PreferenceViewHolder holder,
            final Context context) {
        final Optional<View> summaryView = findViewById(holder, android.R.id.summary);
        if (summaryView.isPresent()) {
            ViewAdder.replaceViewWithViews(
                    summaryView.get(),
                    ImmutableList
                            .<View>builder()
                            .add(summaryView.get())
                            .addAll(views)
                            .build(),
                    context);
            return holder;
        } else {
            final LinearLayout container =
                    createLinearLayoutWithChildren(
                            ImmutableList
                                    .<View>builder()
                                    .add(holder.itemView)
                                    .addAll(views)
                                    .build(),
                            context);
            return PreferenceViewHolder.createInstance(container);
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
