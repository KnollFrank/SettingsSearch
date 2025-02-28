package de.KnollFrank.lib.settingssearch.results;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import androidx.annotation.ColorInt;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.settingssearch.common.Attributes;

public class ViewHighlighter {

    public static void highlightView(final View view, final Duration highlightDuration) {
        final Drawable oldBackground = view.getBackground();
        final @ColorInt int color = Attributes.getColorFromAttr(view.getContext(), android.R.attr.textColorPrimary);
        view.setBackgroundColor(color & 0xffffff | 0x33000000);
        new Handler().postDelayed(
                () -> view.setBackground(oldBackground),
                highlightDuration.toMillis());
    }
}
