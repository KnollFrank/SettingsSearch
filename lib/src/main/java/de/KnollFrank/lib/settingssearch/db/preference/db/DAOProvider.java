package de.KnollFrank.lib.settingssearch.db.preference.db;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenGraphDAO;

public interface DAOProvider {

    SearchablePreferenceScreenGraphDAO searchablePreferenceScreenGraphDAO();

    SearchablePreferenceScreenDAO searchablePreferenceScreenDAO();

    SearchablePreferenceDAO searchablePreferenceDAO();
}
