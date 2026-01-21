package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleConverter;

public class LazyPersistableBundleFactory {

    private LazyPersistableBundleFactory() {
    }

    public static LazyPersistableBundle fromBundle(final PersistableBundle bundle) {
        return new LazyPersistableBundle(new PersistableBundleConverter().convertForward(bundle));
    }
}
