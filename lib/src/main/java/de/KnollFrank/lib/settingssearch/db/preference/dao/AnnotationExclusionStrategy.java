package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

// FK-TODO: remove?
// adapted from https://stackoverflow.com/a/27986860
public class AnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
