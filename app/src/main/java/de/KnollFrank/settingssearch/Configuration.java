package de.KnollFrank.settingssearch;

public record Configuration(boolean addPreferenceToPreferenceFragmentWithSinglePreference,
                            boolean summaryChangingPreference) {

    public Configuration asConfigurationHavingSummaryChangingPreference(final boolean summaryChangingPreference) {
        return new Configuration(
                addPreferenceToPreferenceFragmentWithSinglePreference,
                summaryChangingPreference);
    }

    public Configuration asConfigurationHavingAddPreferenceToPreferenceFragmentWithSinglePreference(final boolean addPreferenceToPreferenceFragmentWithSinglePreference) {
        return new Configuration(
                addPreferenceToPreferenceFragmentWithSinglePreference,
                summaryChangingPreference);
    }
}
