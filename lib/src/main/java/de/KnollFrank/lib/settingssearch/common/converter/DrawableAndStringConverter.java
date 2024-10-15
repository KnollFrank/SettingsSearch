package de.KnollFrank.lib.settingssearch.common.converter;

import static de.KnollFrank.lib.settingssearch.common.converter.BitmapAndBytesConverter.bitmap2Bytes;
import static de.KnollFrank.lib.settingssearch.common.converter.BitmapAndBytesConverter.bytes2Bitmap;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.bitmap2Drawable;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.drawable2Bitmap;
import static de.KnollFrank.lib.settingssearch.common.converter.StringAndBytesConverter.bytes2String;
import static de.KnollFrank.lib.settingssearch.common.converter.StringAndBytesConverter.string2Bytes;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class DrawableAndStringConverter {

    public static String drawable2String(final Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        return bytes2String(bitmap2Bytes(drawable2Bitmap(drawable)));
    }

    public static Drawable string2Drawable(final String string, final Resources resources) {
        if (string == null) {
            return null;
        }
        return bitmap2Drawable(bytes2Bitmap(string2Bytes(string)), resources);
    }
}
