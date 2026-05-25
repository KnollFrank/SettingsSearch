package de.KnollFrank.lib.settingssearch.search.progress;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;

public class ProgressProvider {

    private ProgressProvider() {
    }

    public static String getProgress(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return "processing settings of " + getTitleOrClassName(preferenceScreenOfHostOfActivity);
    }

    private static String getTitleOrClassName(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return preferenceScreenOfHostOfActivity
                .title()
                .map(ProgressProvider::quote)
                .orElseGet(() -> preferenceScreenOfHostOfActivity.hostOfPreferenceScreen().getClass().getSimpleName());
    }

    private static String quote(final String s) {
        return "\"" + s + "\"";
    }
}
