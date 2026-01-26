package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

// FK-TODO: refactor
public class TreeProcessorClassConverter implements Converter<Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>>, String> {

    private static final String LEFT_PREFIX = "L:";
    private static final String RIGHT_PREFIX = "R:";

    @TypeConverter
    @Override
    public String convertForward(final Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> classClassEither) {
        return fromEitherClass(classClassEither);
    }

    @TypeConverter
    @Override
    public Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> convertBackward(final String s) {
        return toEitherClass(s);
    }

    private String fromEitherClass(final Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> either) {
        if (either == null) {
            return null;
        }
        return either.join(
                clazz -> LEFT_PREFIX + clazz.getName(),
                clazz -> RIGHT_PREFIX + clazz.getName()
                          );
    }

    private Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> toEitherClass(final String value) {
        if (value == null) {
            return null;
        }
        try {
            if (value.startsWith(LEFT_PREFIX)) {
                final String className = value.substring(LEFT_PREFIX.length());
                final Class<?> clazz = Class.forName(className);
                @SuppressWarnings("unchecked") final Class<? extends SearchablePreferenceScreenTreeCreator<?>> creatorClass = (Class<? extends SearchablePreferenceScreenTreeCreator<?>>) clazz;
                return Either.ofLeft(creatorClass);
            } else if (value.startsWith(RIGHT_PREFIX)) {
                final String className = value.substring(RIGHT_PREFIX.length());
                final Class<?> clazz = Class.forName(className);
                @SuppressWarnings("unchecked") final Class<? extends SearchablePreferenceScreenTreeTransformer<?>> transformerClass = (Class<? extends SearchablePreferenceScreenTreeTransformer<?>>) clazz;
                return Either.ofRight(transformerClass);
            } else {
                throw new IllegalArgumentException("Invalid value for TreeProcessorClassConverter: " + value);
            }
        } catch (final ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException("Could not convert " + value + " to Either class", e);
        }
    }
}
