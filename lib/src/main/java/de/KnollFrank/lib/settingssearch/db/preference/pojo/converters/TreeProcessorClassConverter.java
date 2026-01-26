package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;

public class TreeProcessorClassConverter implements Converter<Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>>, String> {

    private static final PrefixingClassConverter<SearchablePreferenceScreenTreeCreator<?>> TREE_CREATOR_CLASS_CONVERTER =
            new PrefixingClassConverter<>(
                    new ClassConverter<>(),
                    "L:");
    private static final PrefixingClassConverter<SearchablePreferenceScreenTreeTransformer<?>> TREE_TRANSFORMER_CLASS_CONVERTER =
            new PrefixingClassConverter<>(
                    new ClassConverter<>(),
                    "R:");

    @TypeConverter
    @Override
    public String convertForward(final Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> creatorClassOrTransformerClass) {
        return creatorClassOrTransformerClass.join(
                TREE_CREATOR_CLASS_CONVERTER::convertForward,
                TREE_TRANSFORMER_CLASS_CONVERTER::convertForward);
    }

    @TypeConverter
    @Override
    public Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> convertBackward(final String classNameStartingWithPrefix) {
        if (TREE_CREATOR_CLASS_CONVERTER.canConvertBackward(classNameStartingWithPrefix)) {
            return Either.ofLeft(
                    TREE_CREATOR_CLASS_CONVERTER.convertBackward(
                            classNameStartingWithPrefix));
        } else if (TREE_TRANSFORMER_CLASS_CONVERTER.canConvertBackward(classNameStartingWithPrefix)) {
            return Either.ofRight(
                    TREE_TRANSFORMER_CLASS_CONVERTER.convertBackward(
                            classNameStartingWithPrefix));
        } else {
            throw new IllegalArgumentException("Invalid value for TreeProcessorClassConverter: " + classNameStartingWithPrefix);
        }
    }
}
