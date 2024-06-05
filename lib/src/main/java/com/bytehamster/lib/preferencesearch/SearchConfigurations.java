package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;

class SearchConfigurations {

    private static final String ARGUMENT_FUZZY_ENABLED = "fuzzy";
    private static final String ARGUMENT_SEARCH_BAR_ENABLED = "search_bar_enabled";
    private static final String ARGUMENT_BREADCRUMBS_ENABLED = "breadcrumbs_enabled";
    private static final String ARGUMENT_TEXT_HINT = "text_hint";
    private static final String ARGUMENT_TEXT_NO_RESULTS = "text_no_results";

    public static Bundle toBundle(final SearchConfiguration searchConfiguration) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(ARGUMENT_FUZZY_ENABLED, searchConfiguration.isFuzzySearchEnabled());
        bundle.putBoolean(ARGUMENT_BREADCRUMBS_ENABLED, searchConfiguration.isBreadcrumbsEnabled());
        bundle.putBoolean(ARGUMENT_SEARCH_BAR_ENABLED, searchConfiguration.isSearchBarEnabled());
        bundle.putString(ARGUMENT_TEXT_HINT, searchConfiguration.getTextHint());
        bundle.putString(ARGUMENT_TEXT_NO_RESULTS, searchConfiguration.getTextNoResults());
        return bundle;
    }

    public static SearchConfiguration fromBundle(final Bundle bundle) {
        final SearchConfiguration searchConfiguration = new SearchConfiguration();
        searchConfiguration.setFuzzySearchEnabled(bundle.getBoolean(ARGUMENT_FUZZY_ENABLED));
        searchConfiguration.setBreadcrumbsEnabled(bundle.getBoolean(ARGUMENT_BREADCRUMBS_ENABLED));
        searchConfiguration.setSearchBarEnabled(bundle.getBoolean(ARGUMENT_SEARCH_BAR_ENABLED));
        searchConfiguration.setTextHint(bundle.getString(ARGUMENT_TEXT_HINT));
        searchConfiguration.setTextNoResults(bundle.getString(ARGUMENT_TEXT_NO_RESULTS));
        return searchConfiguration;
    }
}
