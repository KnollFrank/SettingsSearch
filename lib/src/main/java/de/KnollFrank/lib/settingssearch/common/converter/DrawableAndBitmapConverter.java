package de.KnollFrank.lib.settingssearch.common.converter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Size;

import java.util.Optional;

public class DrawableAndBitmapConverter {

    // adapted from https://stackoverflow.com/a/10600736
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof final BitmapDrawable bitmapDrawable) {
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        final Size size = getIntrinsicSize(drawable).orElse(new Size(1, 1));
        final Bitmap bitmap = Bitmap.createBitmap(size.getWidth(), size.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmap2Drawable(final Bitmap bitmap, final Resources resources) {
        return new BitmapDrawable(resources, bitmap);
    }

    private static Optional<Size> getIntrinsicSize(final Drawable drawable) {
        return drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0 ?
                Optional.of(new Size(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())) :
                Optional.empty();
    }
}
