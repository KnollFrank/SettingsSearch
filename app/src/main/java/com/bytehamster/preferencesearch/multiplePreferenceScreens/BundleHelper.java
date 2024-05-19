package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BundleHelper {

    private final Bundle bundle;

    public BundleHelper(Bundle bundle) {
        this.bundle = bundle;
    }

    public Optional<String> getString(final String key) {
        return this.bundle != null ? Optional.ofNullable(this.bundle.getString(key)) : Optional.empty();
    }

    public List<String> getStringArrayList(final String key) {
        if (this.bundle == null || !this.bundle.containsKey(key)) {
            return Collections.emptyList();
        }
        final List<String> result = this.bundle.getStringArrayList(key);
        return result == null ? Collections.emptyList() : result;
    }
}
