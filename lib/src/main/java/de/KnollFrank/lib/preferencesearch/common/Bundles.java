package de.KnollFrank.lib.preferencesearch.common;

import android.os.Bundle;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Bundles {

    private final Bundle bundle;

    public Bundles(final Bundle bundle) {
        this.bundle = bundle;
    }

    public Optional<String> getString(final String key) {
        return this.bundle != null ? Optional.ofNullable(this.bundle.getString(key)) : Optional.empty();
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        return this.bundle != null ? this.bundle.getBoolean(key, defaultValue) : defaultValue;
    }

    public List<String> getStringArrayList(final String key) {
        if (this.bundle == null || !this.bundle.containsKey(key)) {
            return Collections.emptyList();
        }
        final List<String> result = this.bundle.getStringArrayList(key);
        return result == null ? Collections.emptyList() : result;
    }
}
