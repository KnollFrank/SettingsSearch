package de.KnollFrank.lib.settingssearch.client;

import androidx.annotation.IdRes;

import java.util.Optional;

public record SearchConfiguration(
        // FK-TODO: move to SearchConfig
        @IdRes int fragmentContainerViewId,
        // FK-TODO: move to SearchConfig
        Optional<String> queryHint) {
}
