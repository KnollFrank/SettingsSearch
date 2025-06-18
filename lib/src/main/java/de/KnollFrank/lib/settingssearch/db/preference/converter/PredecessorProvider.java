package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

@FunctionalInterface
interface PredecessorProvider {

    Optional<SearchablePreference> getPredecessor(SearchablePreferenceEntity entity);
}
