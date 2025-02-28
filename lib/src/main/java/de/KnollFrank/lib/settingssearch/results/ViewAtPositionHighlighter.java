package de.KnollFrank.lib.settingssearch.results;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.Optional;

class ViewAtPositionHighlighter {

    public static void highlightViewAtPosition(final RecyclerView recyclerView,
                                               final int position,
                                               final Duration highlightDuration,
                                               final Runnable orElse) {
        recyclerView.scrollToPosition(position);
        recyclerView.postDelayed(
                () ->
                        ViewAtPositionHighlighter
                                .getViewAtPosition(recyclerView, position)
                                .ifPresentOrElse(
                                        view -> ViewHighlighter.highlightView(view, highlightDuration),
                                        orElse),
                200);
    }

    private static Optional<View> getViewAtPosition(final RecyclerView recyclerView, final int position) {
        return Optional
                .ofNullable(recyclerView.findViewHolderForAdapterPosition(position))
                .map(viewHolder -> viewHolder.itemView);
    }
}
