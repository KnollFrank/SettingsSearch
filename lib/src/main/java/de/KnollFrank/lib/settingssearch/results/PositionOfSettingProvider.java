package de.KnollFrank.lib.settingssearch.results;

import java.util.OptionalInt;

@FunctionalInterface
public interface PositionOfSettingProvider {

    OptionalInt getPositionOfSetting(final Setting setting);
}
