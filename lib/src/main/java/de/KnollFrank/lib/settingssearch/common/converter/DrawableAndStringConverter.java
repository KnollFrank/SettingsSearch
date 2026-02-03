package de.KnollFrank.lib.settingssearch.common.converter;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static de.KnollFrank.lib.settingssearch.common.converter.BitmapAndBytesConverter.bitmapToBytes;
import static de.KnollFrank.lib.settingssearch.common.converter.BitmapAndBytesConverter.bytesToBitmap;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.bitmapToDrawable;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.drawableToBitmap;
import static de.KnollFrank.lib.settingssearch.common.converter.StringAndBytesConverter.bytesToString;
import static de.KnollFrank.lib.settingssearch.common.converter.StringAndBytesConverter.stringToBytes;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class DrawableAndStringConverter {

    private DrawableAndStringConverter() {
    }

    public static String drawableToString(final Drawable drawable) {
        return bytesToString(encodeBase64(bitmapToBytes(drawableToBitmap(drawable))));
    }

    public static Drawable stringToDrawable(final String string, final Resources resources) {
        return bitmapToDrawable(bytesToBitmap(decodeBase64(stringToBytes(string))), resources);
    }
}
