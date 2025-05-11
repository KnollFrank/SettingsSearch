package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.Bundle;

import java.util.StringJoiner;

public record BundleWithEquality(Bundle bundle) {

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final BundleWithEquality that = (BundleWithEquality) o;
        return bundle.toString().equals(that.bundle.toString());
    }

    @Override
    public int hashCode() {
        return bundle.toString().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BundleWithEquality.class.getSimpleName() + "[", "]")
                .add("bundle=" + bundle)
                .toString();
    }
}
