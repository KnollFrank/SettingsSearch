package de.KnollFrank.lib.preferencesearch.common;

import android.os.Parcel;

public class Parcels {

    public static void writeClass(final Parcel parcel, final Class<?> clazz) {
        parcel.writeString(clazz.getName());
    }

    public static <T> Class<? extends T> readClass(final Parcel parcel) {
        return Utils.getClass(parcel.readString());
    }
}
