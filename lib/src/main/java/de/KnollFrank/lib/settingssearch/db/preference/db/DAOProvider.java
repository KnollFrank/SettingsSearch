package de.KnollFrank.lib.settingssearch.db.preference.db;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceEntityDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;

public interface DAOProvider {

    SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO();

    SearchablePreferenceEntityDAO searchablePreferenceEntityDAO();
}
