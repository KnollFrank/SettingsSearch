package de.KnollFrank.lib.settingssearch.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

public class Attributes {

    private Attributes() {
    }

    public static @ColorInt int getColorFromAttr(final Context context, final int attr) {
        final TypedValue typedValue = getTypedValue(context, attr);
        try (final TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{attr})) {
            return typedArray.getColor(0, 0xff3F51B5);
        }
    }

    private static TypedValue getTypedValue(final Context context, final int attr) {
        final TypedValue typedValue = new TypedValue();
        context
                .getTheme()
                .resolveAttribute(attr, typedValue, true);
        return typedValue;
    }
}
