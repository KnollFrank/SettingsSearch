package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

interface Converter<A, B> {

    B doForward(A a);

    A doBackward(B b);
}
