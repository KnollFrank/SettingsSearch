package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

public interface Converter<A, B> {

    B convertForward(A a);

    A convertBackward(B b);
}
