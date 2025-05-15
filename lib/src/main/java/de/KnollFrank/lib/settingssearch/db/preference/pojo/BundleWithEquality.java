package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

// FK-tODO: remove BundleWithEquality, remove gson
public record BundleWithEquality(Bundle bundle) {

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final BundleWithEquality that = (BundleWithEquality) obj;
        return extractWrappedMap().equals(that.extractWrappedMap());
    }

    @Override
    public int hashCode() {
        return extractWrappedMap().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BundleWithEquality.class.getSimpleName() + "[", "]")
                .add("bundle=" + bundle)
                .toString();
    }

    private Map<String, Object> extractWrappedMap() {
        return bundle
                .keySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                key -> wrapBundle(bundle.get(key))));
    }

    private static Object wrapBundle(final Object obj) {
        return obj instanceof final Bundle bundle ? new BundleWithEquality(bundle) : obj;
    }
}