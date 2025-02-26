package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.settingssearch.common.Attributes;

public class ItemOfRecyclerViewHighlighter implements SettingHighlighter {

    private final RecyclerView recyclerView;
    private final PositionOfSettingProvider positionOfSettingProvider;
    private final Duration highlightDuration;

    public ItemOfRecyclerViewHighlighter(final RecyclerView recyclerView,
                                         final PositionOfSettingProvider positionOfSettingProvider,
                                         final Duration highlightDuration) {
        this.recyclerView = recyclerView;
        this.positionOfSettingProvider = positionOfSettingProvider;
        this.highlightDuration = highlightDuration;
    }

    @Override
    public void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        highlightItem(positionOfSettingProvider.getPositionOfSetting(setting).orElseThrow());
    }

    private void highlightItem(final int itemPosition) {
        new Handler().post(() -> _highlightItem(itemPosition));
    }

    private void _highlightItem(final int itemPosition) {
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
