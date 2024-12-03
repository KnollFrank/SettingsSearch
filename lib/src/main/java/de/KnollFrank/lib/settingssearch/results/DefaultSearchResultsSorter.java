package de.KnollFrank.lib.settingssearch.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class DefaultSearchResultsSorter implements SearchResultsSorter {

    @Override
    public List<SearchablePreferencePOJO> sort(final Collection<SearchablePreferencePOJO> preferences) {
        return new ArrayList<>(preferences);
    }
}
