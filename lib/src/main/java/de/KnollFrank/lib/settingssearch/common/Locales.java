package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;

import java.util.Locale;

public class Locales {

    private Locales() {
    }

    public static Locale getCurrentLocale(final Resources resources) {
        return resources.getConfiguration().getLocales().get(0);
    }
}
