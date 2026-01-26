package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

class PrefixingClassConverter<T> implements Converter<Class<? extends T>, String> {

    private final Converter<Class<? extends T>, String> delegate;
    private final String prefix;

    public PrefixingClassConverter(final Converter<Class<? extends T>, String> delegate,
                                   final String prefix) {
        this.delegate = delegate;
        this.prefix = prefix;
    }

    @Override
    public String convertForward(final Class<? extends T> aClass) {
        final String className = delegate.convertForward(aClass);
        return prefixClassName(className);
    }

    @Override
    public Class<? extends T> convertBackward(final String classNameStartingWithPrefix) {
        final String className = getClassName(classNameStartingWithPrefix);
        return delegate.convertBackward(className);
    }

    public boolean canConvertBackward(final String classNameStartingWithPrefix) {
        return classNameStartingWithPrefix.startsWith(prefix);
    }

    private String prefixClassName(final String className) {
        return prefix + className;
    }

    private String getClassName(final String classNameStartingWithPrefix) {
        return classNameStartingWithPrefix.substring(prefix.length());
    }
}
