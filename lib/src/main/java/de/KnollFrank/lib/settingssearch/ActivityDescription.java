package de.KnollFrank.lib.settingssearch;

import android.app.Activity;
import android.os.PersistableBundle;

import java.util.Objects;

import de.KnollFrank.lib.settingssearch.common.PersistableBundleEquality;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.ActivityDescriptionSurrogate;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.LazyPersistableBundleFactory;

public record ActivityDescription(Class<? extends Activity> activity,
                                  PersistableBundle arguments) {

    public ActivityDescriptionSurrogate asActivityDescriptionSurrogate() {
        return new ActivityDescriptionSurrogate(
                activity,
                LazyPersistableBundleFactory.fromBundle(arguments));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ActivityDescription that = (ActivityDescription) o;
        return Objects.equals(activity, that.activity) &&
                PersistableBundleEquality.areBundlesEqual(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, PersistableBundleEquality.hashCode(arguments));
    }
}
