package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundle;

/** * A fast and simple Room TypeConverter for the {@link LazyPersistableBundle} wrapper.
 * This converter does NOT perform any expensive parsing. It only wraps/unwraps the
 * raw string data.
 */
// FK-TODO: refactor
public class LazyPersistableBundleConverter {

    /**
     * Converts a {@link LazyPersistableBundle} to a {@link String} for database storage.
     * @param lazyBundle The wrapper object.
     * @return The raw XML string, or null.
     */
    @TypeConverter
    public String toDatabase(final LazyPersistableBundle lazyBundle) {
        return lazyBundle == null ? null : lazyBundle.getXmlString();
    }

    /**
     * Converts a {@link String} from the database into a {@link LazyPersistableBundle}.
     * @param xmlString The raw XML string from the database.
     * @return A new {@link LazyPersistableBundle} instance.
     */
    @TypeConverter
    public LazyPersistableBundle fromDatabase(final String xmlString) {
        return LazyPersistableBundle.fromXmlString(xmlString);
    }
}
