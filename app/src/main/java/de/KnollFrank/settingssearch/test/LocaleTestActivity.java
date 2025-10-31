package de.KnollFrank.settingssearch.test;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import de.KnollFrank.settingssearch.PreferenceSearchExample;

public class LocaleTestActivity extends PreferenceSearchExample {

    // FK-TODO: make Optional instead of null
    public static volatile Locale overrideLocale = null;

    @Override
    protected void attachBaseContext(final Context newBase) {
        if (overrideLocale != null) {
            super.attachBaseContext(
                    newBase.createConfigurationContext(
                            getConfigurationWithLocale(overrideLocale)));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    private static Configuration getConfigurationWithLocale(final Locale locale) {
        final Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        return configuration;
    }
}
