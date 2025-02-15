package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.OptionalInt;

import de.KnollFrank.lib.settingssearch.common.Attributes;

// FK-TODO: DRY with PreferenceHighlighter
class ItemOfRecyclerViewHighlighter {

    public static void highlightItemOfRecyclerView(final OptionalInt itemPosition,
                                                   final RecyclerView recyclerView,
                                                   final Duration highlightDuration) {
        itemPosition.ifPresentOrElse(
                _itemPosition -> highlightItemOfRecyclerView(_itemPosition, recyclerView, highlightDuration),
                () -> Log.e("doHighlight", "Setting not found on given screen"));
    }

    private static void highlightItemOfRecyclerView(final int itemPosition,
                                                    final RecyclerView recyclerView,
                                                    final Duration highlightDuration) {
        new Handler().post(() -> _highlightItemOfRecyclerView(itemPosition, recyclerView, highlightDuration));
    }

    private static void _highlightItemOfRecyclerView(final int itemPosition,
                                                     final RecyclerView recyclerView,
                                                     final Duration highlightDuration) {
        recyclerView.scrollToPosition(itemPosition);
        recyclerView.postDelayed(
                () -> {
                    final RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(itemPosition);
                    if (holder != null) {
                        final Drawable oldBackground = holder.itemView.getBackground();
                        final @ColorInt int color = Attributes.getColorFromAttr(recyclerView.getContext(), android.R.attr.textColorPrimary);
                        holder.itemView.setBackgroundColor(color & 0xffffff | 0x33000000);
                        new Handler().postDelayed(
                                () -> holder.itemView.setBackgroundDrawable(oldBackground),
                                highlightDuration.toMillis());
                    }
                },
                200);
    }
}
