package de.KnollFrank.lib.settingssearch;

import android.app.Activity;
import android.os.PersistableBundle;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.ActivityDescriptionSurrogate;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundleFactory;

public record ActivityDescription(Class<? extends Activity> activity,
                                  PersistableBundle arguments) {

    public ActivityDescriptionSurrogate asActivityDescriptionSurrogate() {
        return new ActivityDescriptionSurrogate(
                activity,
                LazyPersistableBundleFactory.fromBundle(arguments));
    }
}
