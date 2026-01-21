package de.KnollFrank.lib.settingssearch.search.progress;

import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.common.Strings;

public class ProgressProvider {

    private ProgressProvider() {
    }

    public static String getProgress(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return "processing settings of " + getTitleOrClassName(preferenceScreenWithHost);
    }

    private static String getTitleOrClassName(final PreferenceScreenWithHost preferenceScreenWithHost) {
        return ProgressProvider
                .getQuotedTitle(preferenceScreenWithHost.preferenceScreen())
                .orElseGet(() -> preferenceScreenWithHost.host().getClass().getSimpleName());
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
