package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Collection;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class DetachedDbDataProviders {

    public static DetachedDbDataProvider merge(final Collection<DetachedDbDataProvider> detachedDbDataProviders) {
        return new DetachedDbDataProvider(
                Maps.merge(
                        detachedDbDataProviders
                                .stream()
                                .map(detachedDbDataProvider -> detachedDbDataProvider.allPreferencesBySearchablePreferenceScreen)
                                .collect(Collectors.toSet())),
                Maps.merge(
                        detachedDbDataProviders
                                .stream()
                                .map(detachedDbDataProvider -> detachedDbDataProvider.hostByPreference)
                                .collect(Collectors.toSet())),
                Maps.merge(
                        detachedDbDataProviders
                                .stream()
                                .map(detachedDbDataProvider -> detachedDbDataProvider.predecessorByPreference)
                                .collect(Collectors.toSet())),
                Maps.merge(
                        detachedDbDataProviders
                                .stream()
                                .map(detachedDbDataProvider -> detachedDbDataProvider.childrenByPreference)
                                .collect(Collectors.toSet())));
    }
}
