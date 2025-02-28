package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import androidx.annotation.ColorInt;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.settingssearch.common.Attributes;

public class ViewHighlighter {

    public static void highlightView(final View view, final Duration highlightDuration) {
        final Drawable background = view.getBackground();
        view.setBackgroundColor(getHighlightColor(view));
        new Handler().postDelayed(
                () -> view.setBackground(background),
                highlightDuration.toMillis());
    }

    private static @ColorInt int getHighlightColor(final View view) {
        return Attributes.getColorFromAttr(view.getContext(), android.R.attr.textColorPrimary) & 0xffffff | 0x33000000;
    }
}
