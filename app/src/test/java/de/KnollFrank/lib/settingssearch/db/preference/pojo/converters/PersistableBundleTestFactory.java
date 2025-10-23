package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import android.os.PersistableBundle;

import de.KnollFrank.settingssearch.Configuration;

public class PersistableBundleTestFactory {

    public static PersistableBundle createSomePersistableBundle() {
        final PersistableBundle bundle = new PersistableBundle();
        bundle.putStringArray("some string key", new String[]{"some string 1", "some string 2"});
        bundle.putInt("some int key", 815);
        return bundle;
    }

    public static Configuration createSomeConfiguration() {
        return new Configuration(true, false);
    }
}
