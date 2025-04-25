package de.KnollFrank.lib.settingssearch.db.preference.dao;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

// adapted from https://stackoverflow.com/a/27986860
public class AnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(final FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(final Class<?> clazz) {
        return false;
    }
}
