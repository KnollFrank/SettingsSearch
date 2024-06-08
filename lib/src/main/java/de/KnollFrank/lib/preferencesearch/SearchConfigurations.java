package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import de.KnollFrank.lib.preferencesearch.common.Bundles;

class SearchConfigurations {

    private static final String ARGUMENT_TEXT_HINT = "text_hint";
    private static final String ARGUMENT_ROOT_PREFERENCE_FRAGMENT = "rootPreferenceFragment";

    public static Bundle toBundle(final SearchConfiguration searchConfiguration) {
        final Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TEXT_HINT, searchConfiguration.getTextHint());
        new Bundles(bundle).putClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT, searchConfiguration.getRootPreferenceFragment());
        return bundle;
    }

    public static SearchConfiguration fromBundle(final Bundle bundle) {
        final SearchConfiguration searchConfiguration = new SearchConfiguration();
        searchConfiguration.setTextHint(bundle.getString(ARGUMENT_TEXT_HINT));
        searchConfiguration.setRootPreferenceFragment(new Bundles(bundle).getClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT));
        return searchConfiguration;
    }
}
