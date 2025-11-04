package de.KnollFrank.lib.settingssearch.common;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Optional;

public class Intents {

    public static Optional<String> getClassName(final Intent intent, final PackageManager packageManager) {
        return Optional
                .ofNullable(intent.resolveActivity(packageManager))
                .map(ComponentName::getClassName);
    }
}
