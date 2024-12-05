package de.KnollFrank.lib.settingssearch.common.compare;

enum CompareResult {

    ARG1_LESS_THAN_ARG2(-1),
    ARG1_EQUAL_TO_ARG2(0),
    ARG1_GREATER_THAN_ARG2(+1);

    public final int value;

    CompareResult(final int value) {
        this.value = value;
    }

    public static CompareResult of(final int value) {
        if (value < 0) {
            return ARG1_LESS_THAN_ARG2;
        } else if (value == 0) {
            return ARG1_EQUAL_TO_ARG2;
        } else {
            return ARG1_GREATER_THAN_ARG2;
        }
    }
}
