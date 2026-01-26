package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

class ConverterComposition<A, B, C, ABConverter extends Converter<A, B>, BCConverter extends Converter<B, C>> implements Converter<A, C> {

    public final ABConverter first;
    public final BCConverter second;

    public ConverterComposition(final ABConverter first, final BCConverter second) {
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
