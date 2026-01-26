package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

class PrefixingClassConverter<T> implements Converter<Class<? extends T>, String> {

    private final PrefixingConverter prefixingConverter;
    private final ConverterComposition<Class<? extends T>, String, String> converterComposition;

    public PrefixingClassConverter(final ClassConverter<T> classConverter,
                                   final PrefixingConverter prefixingConverter) {
        this.converterComposition = new ConverterComposition<>(classConverter, prefixingConverter);
        this.prefixingConverter = prefixingConverter;
    }

    @Override
    public String convertForward(final Class<? extends T> aClass) {
        return converterComposition.convertForward(aClass);
    }

    @Override
    public Class<? extends T> convertBackward(final String s) {
        return converterComposition.convertBackward(s);
    }

    public boolean canConvertBackward(final String string) {
        return prefixingConverter.canConvertBackward(string);
    }
}
