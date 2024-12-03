package de.KnollFrank.lib.settingssearch.client;

import de.KnollFrank.lib.settingssearch.provider.IncludePreferenceInSearchResultsPredicate;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;

public record Search(
        IncludePreferenceInSearchResultsPredicate includePreferenceInSearchResultsPredicate,
        ShowPreferencePathPredicate showPreferencePathPredicate,
        PrepareShow prepareShow) {
}
