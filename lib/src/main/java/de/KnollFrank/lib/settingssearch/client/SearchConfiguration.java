package de.KnollFrank.lib.settingssearch.client;

import java.util.Optional;

public record SearchConfiguration(
        // FK-TODO: move to SearchConfig
        Optional<String> queryHint) {
}
