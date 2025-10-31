package de.KnollFrank.settingssearch.test;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.settingssearch.PreferenceSearchExample;

public class LocalePreferenceSearchExample extends PreferenceSearchExample {

    private static volatile Optional<Locale> locale = Optional.empty();

    public static void setLocale(final Locale locale) {
        LocalePreferenceSearchExample.locale = Optional.of(locale);
    }

    public static void unsetLocale() {
        locale = Optional.empty();
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(
                locale
                        .map(_locale ->
                                newBase.createConfigurationContext(
                                        getConfigurationWithLocale(_locale)))
                        .orElse(newBase));
    }

    private static Configuration getConfigurationWithLocale(final Locale locale) {
        final Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        return configuration;
    }
}
