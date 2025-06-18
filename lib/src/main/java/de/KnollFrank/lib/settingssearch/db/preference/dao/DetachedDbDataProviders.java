package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class DetachedDbDataProviders {

    public static DetachedDbDataProvider merge(final DetachedDbDataProvider detachedDbDataProvider1,
                                               final DetachedDbDataProvider detachedDbDataProvider2) {
        return new DetachedDbDataProvider(
                Maps.merge(
                        List.of(
                                detachedDbDataProvider1.allPreferencesBySearchablePreferenceScreen,
                                detachedDbDataProvider2.allPreferencesBySearchablePreferenceScreen)),
                Maps.merge(
                        List.of(
                                detachedDbDataProvider1.hostByPreference,
                                detachedDbDataProvider2.hostByPreference)),
                Maps.merge(
                        List.of(
                                detachedDbDataProvider1.predecessorByPreference,
                                detachedDbDataProvider2.predecessorByPreference)),
                Maps.merge(
                        List.of(
                                detachedDbDataProvider1.childrenByPreference,
                                detachedDbDataProvider2.childrenByPreference)));
    }
}
