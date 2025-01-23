package de.KnollFrank.lib.settingssearch.common;

import android.content.Intent;

public class Intents {

    public static String getClassName(final Intent intent) {
        return intent.getComponent().getClassName();
    }
}
