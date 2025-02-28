package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.common.Attributes;

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
                                        view -> highlightView(view, highlightDuration),
                                        orElse),
                200);
    }

    private static Optional<View> getViewAtPosition(final RecyclerView recyclerView, final int position) {
        return Optional
                .ofNullable(recyclerView.findViewHolderForAdapterPosition(position))
                .map(viewHolder -> viewHolder.itemView);
    }

    private static void highlightView(final View view, final Duration highlightDuration) {
        final Drawable oldBackground = view.getBackground();
        final @ColorInt int color = Attributes.getColorFromAttr(view.getContext(), android.R.attr.textColorPrimary);
        view.setBackgroundColor(color & 0xffffff | 0x33000000);
        new Handler().postDelayed(
                () -> view.setBackgroundDrawable(oldBackground),
                highlightDuration.toMillis());
    }
}
