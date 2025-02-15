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
class RecyclerViewHighlighter {

    public static void highlightSettingOfSettingsFragment(final RecyclerView recyclerView,
                                                          final OptionalInt positionOfSetting,
                                                          final Duration highlightDuration) {
        positionOfSetting.ifPresentOrElse(
                _positionOfSetting ->
                        highlightSettingOfSettingsFragment(
                                recyclerView,
                                _positionOfSetting,
                                highlightDuration),
                () -> Log.e("doHighlight", "Setting not found on given screen"));
    }

    private static void highlightSettingOfSettingsFragment(final RecyclerView recyclerView,
                                                           final int positionOfSetting,
                                                           final Duration highlightDuration) {
        new Handler().post(() -> highlightSetting(recyclerView, positionOfSetting, highlightDuration));
    }

    private static void highlightSetting(final RecyclerView recyclerView,
                                         final int positionOfSetting,
                                         final Duration highlightDuration) {
        recyclerView.scrollToPosition(positionOfSetting);
        recyclerView.postDelayed(
                () -> {
                    final RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(positionOfSetting);
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
