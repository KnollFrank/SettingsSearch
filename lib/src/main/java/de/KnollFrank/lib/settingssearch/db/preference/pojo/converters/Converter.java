package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

interface Converter<T> {

    String toString(final T value);

    T fromString(final String string);
}
