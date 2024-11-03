package de.KnollFrank.lib.settingssearch.db.preference.dao;

import org.jgrapht.Graph;

import java.io.InputStream;
import java.io.OutputStream;

import de.KnollFrank.lib.settingssearch.common.IOUtils;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.PreferenceScreenWithHostClassPOJO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJOEdge;

// FK-TODO: remove?
public class POJOGraphDAO {

    public static void persist(final Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> pojoGraph,
                               final OutputStream sink) {
        POJOGraph2JSONConverter.pojoGraph2JSON(pojoGraph, IOUtils.getWriter(sink));
    }

    public static Graph<PreferenceScreenWithHostClassPOJO, SearchablePreferencePOJOEdge> load(final InputStream source) {
        return JSON2POJOGraphConverter.json2PojoGraph(IOUtils.getReader(source));
    }
}
