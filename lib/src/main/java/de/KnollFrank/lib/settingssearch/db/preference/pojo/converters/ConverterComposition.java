package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

class ConverterComposition<A, B, C> implements Converter<A, C> {

    private final Converter<A, B> first;
    private final Converter<B, C> second;

    public ConverterComposition(final Converter<A, B> first, final Converter<B, C> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public C convertForward(final A a) {
        return second.convertForward(first.convertForward(a));
    }

    @Override
    public A convertBackward(final C c) {
        return first.convertBackward(second.convertBackward(c));
    }
}
