package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import androidx.annotation.ColorInt;

import java.time.Duration;

import de.KnollFrank.lib.settingssearch.common.Attributes;

public class ViewHighlighter {

    private ViewHighlighter() {
    }

    public static void highlightView(final View view, final Duration highlightDuration) {
        final Drawable background = view.getBackground();
        view.setBackgroundColor(getHighlightColor(view.getContext()));
        new Handler().postDelayed(
                () -> view.setBackground(background),
                highlightDuration.toMillis());
    }

    private static @ColorInt int getHighlightColor(final Context context) {
        return Attributes.getColorFromAttr(context, android.R.attr.textColorPrimary) & 0xffffff | 0x33000000;
    }
}
