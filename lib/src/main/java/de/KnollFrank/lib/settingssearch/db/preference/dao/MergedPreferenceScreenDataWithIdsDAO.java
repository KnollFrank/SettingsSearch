package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenDataWithIds;

class MergedPreferenceScreenDataWithIdsDAO {

    public static void persist(final MergedPreferenceScreenDataWithIds mergedPreferenceScreenDataWithIds,
                               final OutputStream preferences,
                               final OutputStream preferencePathIdsByPreferenceId,
                               final OutputStream hostByPreferenceId) {
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferences(), preferences);
        // FK-TODO: die List<Integer> ohne den letzten Eintrag speichern, falls nachgewiesen werden kann, dass er IMMER gleich dem Key ist.
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.preferencePathIdsByPreferenceId(), preferencePathIdsByPreferenceId);
        JsonDAO.persist(mergedPreferenceScreenDataWithIds.hostByPreferenceId(), hostByPreferenceId);
    }

    public static MergedPreferenceScreenDataWithIds load(final InputStream preferences,
                                                         final InputStream preferencePathIdsByPreferenceId,
                                                         final InputStream hostByPreferenceId) {
        return new MergedPreferenceScreenDataWithIds(
                JsonDAO.load(
                        preferences,
                        new TypeToken<>() {
                        }),
                // FK-TODO: den beim Speichern entfernten letzten Eintrag von List<Integer> hier wieder dazufügen.
                JsonDAO.load(
                        preferencePathIdsByPreferenceId,
                        new TypeToken<>() {
                        }),
                JsonDAO.load(
                        hostByPreferenceId,
                        new TypeToken<>() {
                        }));
    }
}
