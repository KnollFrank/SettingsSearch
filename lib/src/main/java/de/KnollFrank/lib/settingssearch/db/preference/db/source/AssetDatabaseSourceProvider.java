package de.KnollFrank.lib.settingssearch.db.preference.db.source;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class AssetDatabaseSourceProvider implements DatabaseSourceProvider {

    private final File assetFile;
    private final AssetManager assetManager;

    public AssetDatabaseSourceProvider(final File assetFile, final AssetManager assetManager) {
        this.assetFile = assetFile;
        this.assetManager = assetManager;
    }

    @Override
    public Optional<InputStream> getDatabaseSource() {
        try {
            return Optional.of(assetManager.open(assetFile.getPath()));
        } catch (final IOException e) {
            return Optional.empty();
        }
    }
}
