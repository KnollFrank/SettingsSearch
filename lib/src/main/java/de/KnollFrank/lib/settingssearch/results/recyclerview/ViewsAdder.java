package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

class ViewsAdder {

    private ViewsAdder() {
    }

    public static PreferenceViewHolder addViews(final List<View> views,
                                                final PreferenceViewHolder holder,
                                                final Context context) {
        final Optional<View> summaryView = holder.findViewById(android.R.id.summary);
        if (summaryView.isPresent()) {
            ViewAdder.replaceViewWithViews(
                    summaryView.orElseThrow(),
                    ImmutableList
                            .<View>builder()
                            .add(summaryView.orElseThrow())
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
            return new PreferenceViewHolder(container);
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
}
