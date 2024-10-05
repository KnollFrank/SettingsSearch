package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;

class SearchableInfoAttributeConverter {

    public static String convert2POJO(final Optional<String> searchableInfo) {
        return searchableInfo.orElse(null);
    }

    public static Optional<String> convertFromPOJO(final String searchableInfo) {
        return Optional.ofNullable(searchableInfo);
    }
}
