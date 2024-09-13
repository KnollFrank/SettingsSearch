package de.KnollFrank.lib.settingssearch;

import android.os.Bundle;

import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.common.Bundles;

public class SearchConfigurations {

    private static final String ARGUMENT_QUERY_HINT = "query_hint";
    private static final String ARGUMENT_ROOT_PREFERENCE_FRAGMENT = "rootPreferenceFragment";
    private static final String ARGUMENT_FRAGMENT_CONTAINER_VIEW_ID = "fragmentContainerViewId";

    public static Bundle toBundle(final SearchConfiguration searchConfiguration) {
        final Bundle bundle = new Bundle();
        final Bundles bundles = new Bundles(bundle);
        bundles.putOptionalString(ARGUMENT_QUERY_HINT, searchConfiguration.queryHint());
        bundles.putClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT, searchConfiguration.rootPreferenceFragment());
        bundle.putInt(ARGUMENT_FRAGMENT_CONTAINER_VIEW_ID, searchConfiguration.fragmentContainerViewId());
        return bundle;
    }

    public static SearchConfiguration fromBundle(final Bundle bundle) {
        final Bundles bundles = new Bundles(bundle);
        return new SearchConfiguration(
                bundle.getInt(ARGUMENT_FRAGMENT_CONTAINER_VIEW_ID),
                bundles.getOptionalString(ARGUMENT_QUERY_HINT),
                bundles.getClass(ARGUMENT_ROOT_PREFERENCE_FRAGMENT));
    }
}
