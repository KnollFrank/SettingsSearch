package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.app.Activity;

import de.KnollFrank.lib.settingssearch.ActivityDescription;

public record ActivityDescriptionSurrogate(Class<? extends Activity> activity,
                                           // FK-TODO: change to Optional<LazyPersistableBundle>
                                           LazyPersistableBundle arguments) {

    public ActivityDescription asActivityDescription() {
        return new ActivityDescription(
                activity,
                arguments.get());
    }
}
