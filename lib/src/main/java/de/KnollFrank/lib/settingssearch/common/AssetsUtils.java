package de.KnollFrank.lib.settingssearch.common;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {

    public static InputStream open(final AssetManager assets, final File filename) {
        try {
            return assets.open(filename.getPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String[] list(final AssetManager assets, final File path) {
        try {
            return assets.list(path.getPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
