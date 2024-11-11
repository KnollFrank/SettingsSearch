package de.KnollFrank.lib.settingssearch.common.converter;

import static de.KnollFrank.lib.settingssearch.common.converter.BitmapAndBytesConverter.bitmap2Bytes;
import static de.KnollFrank.lib.settingssearch.common.converter.BitmapAndBytesConverter.bytes2Bitmap;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.bitmap2Drawable;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.drawable2Bitmap;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.Optional;

public class DrawableAndBytesConverter {

    public static Optional<byte[]> drawable2Bytes(final Optional<Drawable> drawable) {
        return drawable.map(DrawableAndBytesConverter::drawable2Bytes);
    }

    public static Optional<Drawable> bytes2Drawable(final Optional<byte[]> bytes, final Resources resources) {
        return bytes.map(s -> bytes2Drawable(s, resources));
    }

    private static byte[] drawable2Bytes(final Drawable drawable) {
        return bitmap2Bytes(drawable2Bitmap(drawable));
    }

    private static Drawable bytes2Drawable(final byte[] bytes, final Resources resources) {
        return bitmap2Drawable(bytes2Bitmap(bytes), resources);
    }
}
