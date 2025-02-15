package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.settingssearch.common.Attributes;

// FK-TODO: DRY with PreferenceHighlighter
class SettingHighlighter {

    public static void highlightSettingOfSettingsFragment(
            final String keyOfSetting,
            final SettingsFragment settingsFragment,
            final Duration highlightDuration) {
        settingsFragment
                .getPositionOfSetting(keyOfSetting)
                .ifPresentOrElse(
                        positionOfSetting ->
                                highlightSettingOfSettingsFragment(
                                        positionOfSetting,
                                        settingsFragment,
                                        highlightDuration),
                        () -> Log.e("doHighlight", "Setting not found on given screen"));
    }

    private static void highlightSettingOfSettingsFragment(
            final int positionOfSetting,
            final SettingsFragment settingsFragment,
            final Duration highlightDuration) {
        new Handler().post(() -> highlightSetting(positionOfSetting, settingsFragment, highlightDuration));
    }

    private static void highlightSetting(final int positionOfSetting,
                                         final SettingsFragment settingsFragment,
                                         final Duration highlightDuration) {
        final RecyclerView recyclerView = settingsFragment.getRecyclerView();
        recyclerView.scrollToPosition(positionOfSetting);
        recyclerView.postDelayed(
                () -> {
                    final RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(positionOfSetting);
                    if (holder != null) {
                        final Drawable oldBackground = holder.itemView.getBackground();
                        final @ColorInt int color = Attributes.getColorFromAttr(((Fragment) settingsFragment).getContext(), android.R.attr.textColorPrimary);
                        holder.itemView.setBackgroundColor(color & 0xffffff | 0x33000000);
                        new Handler().postDelayed(
                                () -> holder.itemView.setBackgroundDrawable(oldBackground),
                                highlightDuration.toMillis());
                    }
                },
                200);
    }
}
