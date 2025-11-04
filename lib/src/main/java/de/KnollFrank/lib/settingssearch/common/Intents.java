package de.KnollFrank.lib.settingssearch.common;

import android.content.ComponentName;
import android.content.Intent;

import java.util.Optional;

public class Intents {

    public static Optional<String> getClassName(final Intent intent) {
        return Optional
                .ofNullable(intent.getComponent())
                .map(ComponentName::getClassName);
    }
}
