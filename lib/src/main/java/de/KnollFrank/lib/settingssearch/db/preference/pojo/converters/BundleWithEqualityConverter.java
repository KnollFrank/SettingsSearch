package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.Bundle;

import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.BundleWithEquality;

public class BundleWithEqualityConverter implements Converter<BundleWithEquality, String> {

    private final Converter<Bundle, String> bundleConverter = new BundleConverter();

    @TypeConverter
    @Override
    public String doForward(final BundleWithEquality bundleWithEquality) {
        return bundleConverter.doForward(bundleWithEquality.bundle());
    }

    @TypeConverter
    @Override
    public BundleWithEquality doBackward(final String string) {
        return new BundleWithEquality(bundleConverter.doBackward(string));
    }
}
