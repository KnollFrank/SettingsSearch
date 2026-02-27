package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import android.content.res.AssetManager;

import java.io.File;
import java.io.InputStream;

import de.KnollFrank.lib.settingssearch.common.AssetsUtils;

public class AssetDatabaseSourceProvider implements DatabaseSourceProvider {

    private final File assetFile;
    private final AssetManager assetManager;

    public AssetDatabaseSourceProvider(final File assetFile, final AssetManager assetManager) {
        this.assetFile = assetFile;
        this.assetManager = assetManager;
    }

    @Override
    public InputStream getDatabaseSource() {
        return AssetsUtils.open(assetFile, assetManager);
    }
}
