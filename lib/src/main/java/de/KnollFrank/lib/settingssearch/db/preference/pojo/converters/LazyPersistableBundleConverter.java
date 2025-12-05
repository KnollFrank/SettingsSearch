package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundle;

public class LazyPersistableBundleConverter implements Converter<LazyPersistableBundle, String> {

    @Override
    @TypeConverter
    public String convertForward(final LazyPersistableBundle lazyPersistableBundle) {
        return lazyPersistableBundle.getXmlString();
    }

    @Override
    @TypeConverter
    public LazyPersistableBundle convertBackward(final String xmlString) {
        return new LazyPersistableBundle(xmlString);
    }
}
