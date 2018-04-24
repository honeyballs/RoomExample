package de.thm.roomexample.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

@Entity(tableName = "mitarbeiter", foreignKeys = @ForeignKey(entity = Abteilung.class, parentColumns = "id", childColumns = "abt_id"))
public class Mitarbeiter {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "abt_id")
    private int abtId;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String position;

    private int salary;

    private Date birthday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAbtId() {
        return abtId;
    }

    public void setAbtId(int abtId) {
        this.abtId = abtId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
