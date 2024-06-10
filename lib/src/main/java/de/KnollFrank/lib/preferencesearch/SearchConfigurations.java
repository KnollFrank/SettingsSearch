package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;
import android.view.View;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Bundles;

class SearchConfigurations {

    private static final String ARGUMENT_TEXT_HINT = "text_hint";
    private static final String ARGUMENT_ROOT_PREFERENCE_FRAGMENT = "rootPreferenceFragment";

    public static Bundle toBundle(final SearchConfiguration searchConfiguration) {
        final Bundle bundle = new Bundle();
        final Bundles bundles = new Bundles(bundle);
        bundles.putOptionalString(ARGUMENT_TEXT_HINT, searchConfiguration.textHint);
        bundles.putClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT, searchConfiguration.rootPreferenceFragment);
        return bundle;
    }

    public static SearchConfiguration fromBundle(final Bundle bundle) {
        final Bundles bundles = new Bundles(bundle);
        return new SearchConfiguration(
                Optional.empty(),
                View.NO_ID,
                bundles.getOptionalString(ARGUMENT_TEXT_HINT),
                bundles.getClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT));
    }
}
