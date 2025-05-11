package de.KnollFrank.lib.settingssearch.db.preference.db;

import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceScreenDAO;

public interface DAOProvider {

    SearchablePreferenceScreenDAO searchablePreferenceScreenDAO();

    SearchablePreferenceDAO searchablePreferenceDAO();
}
