package de.KnollFrank.lib.settingssearch.search;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

public record SearchPreferenceFragmentLayout(@LayoutRes int contentLayoutId,
                                             @IdRes int searchViewId,
                                             @IdRes int searchResultsFragmentContainerViewId,
                                             @IdRes int progressContainerId,
                                             @IdRes int progressTextId) {
}
