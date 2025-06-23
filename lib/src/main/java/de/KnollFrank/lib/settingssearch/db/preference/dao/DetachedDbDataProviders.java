package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Collection;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;

// FK-TODO: remove class
public class DetachedDbDataProviders {

    public static DetachedDbDataProvider merge(final Collection<DetachedDbDataProvider> detachedDbDataProviders) {
        return new DetachedDbDataProvider(
                new DbDataProviderData(
                        Maps.merge(
                                detachedDbDataProviders
                                        .stream()
                                        .map(detachedDbDataProvider -> detachedDbDataProvider.dbDataProviderData.allPreferencesBySearchablePreferenceScreen())
                                        .collect(Collectors.toSet())),
                        Maps.merge(
                                detachedDbDataProviders
                                        .stream()
                                        .map(detachedDbDataProvider -> detachedDbDataProvider.dbDataProviderData.hostByPreference())
                                        .collect(Collectors.toSet())),
                        Maps.merge(
                                detachedDbDataProviders
                                        .stream()
                                        .map(detachedDbDataProvider -> detachedDbDataProvider.dbDataProviderData.predecessorByPreference())
                                        .collect(Collectors.toSet())),
                        Maps.merge(
                                detachedDbDataProviders
                                        .stream()
                                        .map(detachedDbDataProvider -> detachedDbDataProvider.dbDataProviderData.childrenByPreference())
                                        .collect(Collectors.toSet()))));
    }
}
