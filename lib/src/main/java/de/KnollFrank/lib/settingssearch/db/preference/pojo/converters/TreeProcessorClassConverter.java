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
                .join(className -> LEFT_PREFIX + className,
                      className -> RIGHT_PREFIX + className);
    }

    @TypeConverter
    @Override
    public Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> convertBackward(final String string) {
        if (string.startsWith(LEFT_PREFIX)) {
            return Either.ofLeft(
                    treeCreatorClassConverter.convertBackward(
                            string.substring(LEFT_PREFIX.length())));
        } else if (string.startsWith(RIGHT_PREFIX)) {
            return Either.ofRight(
                    treeTransformerClassConverter.convertBackward(
                            string.substring(RIGHT_PREFIX.length())));
        } else {
            throw new IllegalArgumentException("Invalid value for TreeProcessorClassConverter: " + string);
        }
    }
}
