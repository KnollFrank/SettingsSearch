package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundle;

public class LazyPersistableBundleConverter implements Converter<LazyPersistableBundle, String> {

    @TypeConverter
    @Override
    public String convertForward(final LazyPersistableBundle lazyPersistableBundle) {
        return lazyPersistableBundle.getXmlString();
    }

    @TypeConverter
    @Override
    public LazyPersistableBundle convertBackward(final String xmlString) {
        return new LazyPersistableBundle(xmlString);
    }
}
