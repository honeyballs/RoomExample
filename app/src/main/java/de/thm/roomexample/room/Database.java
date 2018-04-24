package de.thm.roomexample.room;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

@android.arch.persistence.room.Database(entities = {Abteilung.class, Mitarbeiter.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract AbteilungDao abteilungDao();
    public abstract MitarbeiterDao mitarbeiterDao();

    public static Database getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "abteilungs_db").build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
