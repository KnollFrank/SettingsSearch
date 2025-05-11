package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.StringJoiner;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.BundleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.Converter;

public record BundleWithEquality(Bundle bundle) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final BundleWithEquality that = (BundleWithEquality) o;
        return asString(bundle).equals(asString(that.bundle));
    }

    @Override
    public int hashCode() {
        return asString(bundle).hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BundleWithEquality.class.getSimpleName() + "[", "]")
                .add("bundle=" + bundle)
                .toString();
    }

    private static final Converter<Bundle, String> bundleConverter = new BundleConverter();

    private static String asString(final Bundle bundle) {
        return bundleConverter.doForward(bundle);
    }
}
