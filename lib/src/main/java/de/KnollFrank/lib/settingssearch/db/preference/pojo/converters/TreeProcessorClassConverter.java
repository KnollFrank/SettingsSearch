package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

// FK-TODO: refactor
public class TreeProcessorClassConverter implements Converter<Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>>, String> {

    private final Converter<Class<?>, String> classConverter = new ClassConverter();

    private static final String LEFT_PREFIX = "L:";
    private static final String RIGHT_PREFIX = "R:";

    @TypeConverter
    @Override
    public String convertForward(final Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> creatorClassOrTransformerClass) {
        return creatorClassOrTransformerClass
                .map(classConverter::convertForward,
                     classConverter::convertForward)
                .join(className -> LEFT_PREFIX + className,
                      className -> RIGHT_PREFIX + className);
    }

    @TypeConverter
    @Override
    public Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> convertBackward(final String string) {
        if (string.startsWith(LEFT_PREFIX)) {
            final String className = string.substring(LEFT_PREFIX.length());
            return Either.ofLeft((Class<? extends SearchablePreferenceScreenTreeCreator<?>>) classConverter.convertBackward(className));
        } else if (string.startsWith(RIGHT_PREFIX)) {
            final String className = string.substring(RIGHT_PREFIX.length());
            return Either.ofRight((Class<? extends SearchablePreferenceScreenTreeTransformer<?>>) classConverter.convertBackward(className));
        } else {
            throw new IllegalArgumentException("Invalid value for TreeProcessorClassConverter: " + string);
        }
    }
}
