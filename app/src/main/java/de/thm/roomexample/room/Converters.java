package de.thm.roomexample.room;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

public class Converters {

    //We define converters to convert date to timestamps since a date cannot be saved in the db.

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
