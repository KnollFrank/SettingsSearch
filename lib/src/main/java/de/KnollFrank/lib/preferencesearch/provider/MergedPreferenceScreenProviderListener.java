package de.KnollFrank.lib.preferencesearch.provider;

public interface MergedPreferenceScreenProviderListener {

    void onStartGetMergedPreferenceScreen(final String preferenceFragment);

    void onFinishGetMergedPreferenceScreen(final String preferenceFragment);
}
