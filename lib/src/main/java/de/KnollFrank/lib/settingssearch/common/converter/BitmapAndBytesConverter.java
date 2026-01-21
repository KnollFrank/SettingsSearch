package de.KnollFrank.lib.settingssearch.common.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

class BitmapAndBytesConverter {

    private BitmapAndBytesConverter() {
    }

    public static byte[] bitmap2Bytes(final Bitmap bitmap) {
        final ByteArrayOutputStream sink = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, sink);
        return sink.toByteArray();
    }

    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
