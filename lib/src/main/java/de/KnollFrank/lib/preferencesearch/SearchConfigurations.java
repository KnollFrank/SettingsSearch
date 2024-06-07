package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import de.KnollFrank.lib.preferencesearch.common.Bundles;

class SearchConfigurations {

    private static final String ARGUMENT_BREADCRUMBS_ENABLED = "breadcrumbs_enabled";
    private static final String ARGUMENT_TEXT_HINT = "text_hint";
    private static final String ARGUMENT_TEXT_NO_RESULTS = "text_no_results";
    private static final String ARGUMENT_ROOT_PREFERENCE_FRAGMENT = "rootPreferenceFragment";

    public static Bundle toBundle(final SearchConfiguration searchConfiguration) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(ARGUMENT_BREADCRUMBS_ENABLED, searchConfiguration.isBreadcrumbsEnabled());
        bundle.putString(ARGUMENT_TEXT_HINT, searchConfiguration.getTextHint());
        bundle.putString(ARGUMENT_TEXT_NO_RESULTS, searchConfiguration.getTextNoResults());
        new Bundles(bundle).putClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT, searchConfiguration.getRootPreferenceFragment());
        return bundle;
    }

    public static SearchConfiguration fromBundle(final Bundle bundle) {
        final SearchConfiguration searchConfiguration = new SearchConfiguration();
        searchConfiguration.setBreadcrumbsEnabled(bundle.getBoolean(ARGUMENT_BREADCRUMBS_ENABLED));
        searchConfiguration.setTextHint(bundle.getString(ARGUMENT_TEXT_HINT));
        searchConfiguration.setTextNoResults(bundle.getString(ARGUMENT_TEXT_NO_RESULTS));
        searchConfiguration.setRootPreferenceFragment(new Bundles(bundle).getClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT));
        return searchConfiguration;
    }
}
