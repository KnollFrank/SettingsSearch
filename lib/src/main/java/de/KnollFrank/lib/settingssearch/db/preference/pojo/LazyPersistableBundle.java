package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.annotation.SuppressLint;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A wrapper for a PersistableBundle that delays (makes lazy) its expensive deserialization.
 * This class is immutable and thread-safe. It is designed to solve performance issues
 * when loading many objects with PersistableBundles from a Room database.
 */
// FK-TODO: refactor
@SuppressLint("NewApi") // PersistableBundle requires API 21, this silences the lint check.
public final class LazyPersistableBundle implements Serializable {

    /**
     * The raw XML string data, directly from the database.
     * This is the only state that is part of the object's identity.
     */
    @Nullable
    private final String xmlString;

    /**
     * The cache for the deserialized bundle.
     * It is marked 'transient' so it is not included in any serialization of this object.
     * It is marked 'volatile' to ensure visibility across threads for the double-checked locking pattern.
     */
    @Nullable
    private transient volatile PersistableBundle bundleCache;

    /**
     * Private constructor to be used by the static factory methods.
     * @param xmlString The raw XML string, can be null.
     */
    private LazyPersistableBundle(@Nullable final String xmlString) {
        this.xmlString = xmlString;
    }

    /**
     * Returns the deserialized {@link PersistableBundle}.
     * The expensive deserialization is performed only on the first call.
     * Subsequent calls return a cached instance. This method is thread-safe.
     *
     * @return A non-null, possibly empty, {@link PersistableBundle}.
     */
    @NonNull
    public PersistableBundle get() {
        // Double-checked locking for maximum performance in a multi-threaded context.
        // First check avoids the synchronization overhead if the cache is already populated.
        PersistableBundle localCache = bundleCache;
        if (localCache == null) {
            synchronized (this) {
                localCache = bundleCache;
                if (localCache == null) {
                    if (xmlString == null || xmlString.isEmpty()) {
                        // If there is no data, create an empty bundle.
                        localCache = PersistableBundle.EMPTY;
                    } else {
                        // Perform the expensive deserialization inside the synchronized block.
                        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))) {
                            localCache = PersistableBundle.readFromStream(inputStream);
                        } catch (final IOException e) {
                            // In a real-world scenario, consider a more robust error handling
                            // than just crashing the app.
                            throw new IllegalStateException("Failed to lazily deserialize PersistableBundle", e);
                        }
                    }
                    bundleCache = localCache;
                }
            }
        }
        return localCache;
    }

    /**
     * Returns the raw XML string. Intended for use by the TypeConverter.
     */
    @Nullable
    public String getXmlString() {
        return this.xmlString;
    }

    /**
     * Factory method to create an instance from a raw XML string.
     * Intended for use by the TypeConverter.
     */
    @NonNull
    public static LazyPersistableBundle fromXmlString(@Nullable final String xmlString) {
        return new LazyPersistableBundle(xmlString);
    }

    /**
     * Factory method to create an instance from an existing {@link PersistableBundle}.
     * This performs the serialization immediately.
     */
    @NonNull
    public static LazyPersistableBundle fromBundle(@Nullable final PersistableBundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return new LazyPersistableBundle(null);
        }
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            bundle.writeToStream(outputStream);
            final String xml = outputStream.toString(StandardCharsets.UTF_8);
            return new LazyPersistableBundle(xml);
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to serialize PersistableBundle for lazy wrapper", e);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LazyPersistableBundle that = (LazyPersistableBundle) o;
        return Objects.equals(this.xmlString, that.xmlString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.xmlString);
    }

    @NonNull
    @Override
    public String toString() {
        return "LazyPersistableBundle{" +
                "xmlString='" + (xmlString != null ? "present" : "null") + '\'' +
                ", cached=" + (bundleCache != null) +
                '}';
    }
}
