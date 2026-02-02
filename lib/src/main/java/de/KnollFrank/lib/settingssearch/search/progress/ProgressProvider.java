package de.KnollFrank.lib.settingssearch.search.progress;

import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Strings;

public class ProgressProvider {

    private ProgressProvider() {
    }

    public static String getProgress(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return "processing settings of " + getTitleOrClassName(preferenceScreenOfHostOfActivity);
    }

    private static String getTitleOrClassName(final PreferenceScreenOfHostOfActivity preferenceScreenOfHostOfActivity) {
        return ProgressProvider
                .getQuotedTitle(preferenceScreenOfHostOfActivity.preferenceScreen())
                .orElseGet(() -> preferenceScreenOfHostOfActivity.hostOfPreferenceScreen().getClass().getSimpleName());
    }

    private static Optional<String> getQuotedTitle(final PreferenceScreen preferenceScreen) {
        return Strings
                .toString(Optional.ofNullable(preferenceScreen.getTitle()))
                .map(ProgressProvider::quote);
    }

    private static String quote(final String s) {
        return "\"" + s + "\"";
    }
}
