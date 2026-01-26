package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

class PrefixingClassConverter<T> implements Converter<Class<? extends T>, String> {

    private final ConverterComposition<Class<? extends T>, String, String, ClassConverter<T>, PrefixingConverter> converterComposition;

    private PrefixingClassConverter(final ConverterComposition<Class<? extends T>, String, String, ClassConverter<T>, PrefixingConverter> converterComposition) {
        this.converterComposition = converterComposition;
    }

    public static <T> PrefixingClassConverter<T> of(final ClassConverter<T> classConverter,
                                                    final PrefixingConverter prefixingConverter) {
        return new PrefixingClassConverter<>(
                new ConverterComposition<>(
                        classConverter,
                        prefixingConverter));

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
        return converterComposition.second.canConvertBackward(string);
    }
}
