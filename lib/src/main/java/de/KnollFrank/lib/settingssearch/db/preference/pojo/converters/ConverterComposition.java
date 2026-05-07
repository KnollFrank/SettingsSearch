package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

record ConverterComposition<
        A,
        B,
        C,
        ABConverter extends Converter<A, B>,
        BCConverter extends Converter<B, C>>(
        ABConverter first,
        BCConverter second)
        implements Converter<A, C> {

    @Override
    public C convertForward(final A a) {
        return second.convertForward(first.convertForward(a));
    }

    @Override
    public A convertBackward(final C c) {
        return first.convertBackward(second.convertBackward(c));
    }
}
