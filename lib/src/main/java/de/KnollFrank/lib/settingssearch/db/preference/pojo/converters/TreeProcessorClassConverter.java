package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public class TreeProcessorClassConverter implements Converter<Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>>, String> {

    private static final Converter<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, String> treeCreatorClassConverter = new ClassConverter<>();
    private static final Converter<Class<? extends SearchablePreferenceScreenTreeTransformer<?>>, String> treeTransformerClassConverter = new ClassConverter<>();

    private static final String LEFT_PREFIX = "L:";
    private static final String RIGHT_PREFIX = "R:";

    @TypeConverter
    @Override
    public String convertForward(final Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> creatorClassOrTransformerClass) {
        return creatorClassOrTransformerClass
                .map(treeCreatorClassConverter::convertForward,
                     treeTransformerClassConverter::convertForward)
                .join(className -> wrapClassName(className, LEFT_PREFIX),
                      className -> wrapClassName(className, RIGHT_PREFIX));
    }

    @TypeConverter
    @Override
    public Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> convertBackward(final String classNameStartingWithPrefix) {
        return TreeProcessorClassConverter
                .unwrapClassName(classNameStartingWithPrefix)
                .map(treeCreatorClassConverter::convertBackward,
                     treeTransformerClassConverter::convertBackward);

    }

    private static String wrapClassName(final String className, final String prefix) {
        return prefix + className;
    }

    private static Either<String, String> unwrapClassName(final String classNameStartingWithPrefix) {
        if (classNameStartingWithPrefix.startsWith(LEFT_PREFIX)) {
            return Either.ofLeft(getClassName(classNameStartingWithPrefix, LEFT_PREFIX));
        } else if (classNameStartingWithPrefix.startsWith(RIGHT_PREFIX)) {
            return Either.ofRight(getClassName(classNameStartingWithPrefix, RIGHT_PREFIX));
        } else {
            throw new IllegalArgumentException("Invalid value for TreeProcessorClassConverter: " + classNameStartingWithPrefix);
        }
    }

    private static String getClassName(final String classNameStartingWithPrefix, final String prefix) {
        return classNameStartingWithPrefix.substring(prefix.length());
    }
}
