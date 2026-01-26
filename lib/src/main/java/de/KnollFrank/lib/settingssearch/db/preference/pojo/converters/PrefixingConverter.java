package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

class PrefixingConverter implements Converter<String, String> {

    private final String prefix;

    public PrefixingConverter(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String convertForward(final String string) {
        return prefix(string);
    }

    @Override
    public String convertBackward(final String stringStartingWithPrefix) {
        return unprefix(stringStartingWithPrefix);
    }

    public boolean canConvertBackward(final String string) {
        return canUnprefix(string);
    }

    private String prefix(final String string) {
        return prefix + string;
    }

    private String unprefix(final String stringStartingWithPrefix) {
        return stringStartingWithPrefix.substring(prefix.length());
    }

    private boolean canUnprefix(final String string) {
        return string.startsWith(prefix);
    }
}
