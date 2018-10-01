package de.thm.roomexample.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

@Dao
public interface AbteilungDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAbteilungen(Abteilung...abteilungen);

    @Delete
    public void removeAbteilung(Abteilung abteilung);

    @Update
    public void updateAbteilungen(Abteilung...abteilungen);

    @Query("SELECT * FROM abteilungen ORDER BY name ASC")
    public List<Abteilung> getAllAbteilungen();

    @Query("SELECT * FROM abteilungen WHERE id = :id")
    public Abteilung getAbteilungById(int id);

}
