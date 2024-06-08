package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Bundles;

class SearchConfigurations {

    private static final String ARGUMENT_TEXT_HINT = "text_hint";
    private static final String ARGUMENT_ROOT_PREFERENCE_FRAGMENT = "rootPreferenceFragment";

    public static Bundle toBundle(final SearchConfiguration searchConfiguration) {
        final Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TEXT_HINT, searchConfiguration.textHint);
        new Bundles(bundle).putClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT, searchConfiguration.rootPreferenceFragment);
        return bundle;
    }

    public static SearchConfiguration fromBundle(final Bundle bundle) {
        return new SearchConfiguration(
                Optional.empty(),
                0,
                bundle.getString(ARGUMENT_TEXT_HINT),
                new Bundles(bundle).getClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT));
    }
}
