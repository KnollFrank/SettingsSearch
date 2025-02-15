package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.OptionalInt;

import de.KnollFrank.lib.settingssearch.common.Attributes;

public class ItemOfRecyclerViewHighlighter implements SettingHighlighter {

    private final RecyclerView recyclerView;
    private final PositionOfSettingProvider positionOfSettingProvider;

    public ItemOfRecyclerViewHighlighter(final RecyclerView recyclerView,
                                         final PositionOfSettingProvider positionOfSettingProvider) {
        this.recyclerView = recyclerView;
        this.positionOfSettingProvider = positionOfSettingProvider;
    }

    @Override
    public void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        highlightItemOfRecyclerView(
                positionOfSettingProvider.getPositionOfSetting(setting),
                Duration.ofSeconds(1));
    }

    private void highlightItemOfRecyclerView(final OptionalInt itemPosition, final Duration highlightDuration) {
        itemPosition.ifPresentOrElse(
                _itemPosition -> highlightItemOfRecyclerView(_itemPosition, highlightDuration),
                () -> Log.e("doHighlight", "Setting not found on given screen"));
    }

    private void highlightItemOfRecyclerView(final int itemPosition, final Duration highlightDuration) {
        new Handler().post(() -> _highlightItemOfRecyclerView(itemPosition, highlightDuration));
    }

    private void _highlightItemOfRecyclerView(final int itemPosition, final Duration highlightDuration) {
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
