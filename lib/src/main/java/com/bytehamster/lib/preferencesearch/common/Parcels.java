package com.bytehamster.lib.preferencesearch.common;

import android.os.Parcel;

public class Parcels {

    public static void writeClass(final Parcel parcel, final Class<?> clazz) {
        parcel.writeString(clazz.getName());
    }

    public static <T> Class<? extends T> readClass(final Parcel parcel) {
        try {
            return (Class<? extends T>) Class.forName(parcel.readString());
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
