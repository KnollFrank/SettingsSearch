package de.KnollFrank.lib.settingssearch.common;

import android.os.Bundle;

import java.util.Optional;
import java.util.OptionalInt;

public class Bundles {

    private final Bundle bundle;

    public Bundles(final Bundle bundle) {
        this.bundle = bundle;
    }

    public static Bundle merge(final Bundle bundle1, final Bundle bundle2) {
        final Bundle bundle = new Bundle();
        bundle.putAll(bundle1);
        bundle.putAll(bundle2);
        return bundle;
    }

    public void putOptionalString(final String key, final Optional<String> value) {
        value.ifPresent(_value -> bundle.putString(key, _value));
    }

    public Optional<String> getOptionalString(final String key) {
        return Optional.ofNullable(this.bundle.getString(key));
    }

    public OptionalInt getOptionalInt(final String key) {
        return bundle.containsKey(key) ?
                OptionalInt.of(bundle.getInt(key)) :
                OptionalInt.empty();
    }

    public <T> void putClass(final String key, final Class<? extends T> value) {
        this.bundle.putString(key, value.getName());
    }

    public <T> Class<? extends T> getClass(final String key) {
        return Classes.getClass(this.bundle.getString(key));
    }
}
