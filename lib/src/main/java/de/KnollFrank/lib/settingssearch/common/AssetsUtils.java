package de.KnollFrank.lib.settingssearch.common;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {

    public static InputStream open(final File assetFile, final AssetManager assetManager) {
        try {
            return assetManager.open(assetFile.getPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String[] list(final File path, final AssetManager assetManager) {
        try {
            return assetManager.list(path.getPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean assetExists(final File assetFile, final AssetManager assetManager) {
        try (final InputStream ignored = assetManager.open(assetFile.getPath())) {
            return true;
        } catch (final IOException e) {
            return false;
        }
    }
}
