# RoomExample

A small example app to showcase how to use the Room database API. You can find all Room related files in the `room` package.

## Add dependencies

Add these dependencies to the `build.gradle` file at app level:

````groovy
implementation "android.arch.persistence.room:runtime:1.0.0"
annotationProcessor "android.arch.persistence.room:compiler:1.0.0"
````

The complete `build.gradle` file:

````groovy
apply plugin: 'com.android.application'

android {
    compileSdkVersion 27
    defaultConfig {
        applicationId "de.thm.roomexample"
        minSdkVersion 15
        targetSdkVersion 27
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            postprocessing {
                removeUnusedCode false
                removeUnusedResources false
                obfuscate false
                optimizeCode false
                proguardFile 'proguard-rules.pro'
            }
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation "android.arch.persistence.room:runtime:1.0.0"
    annotationProcessor "android.arch.persistence.room:compiler:1.0.0"
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
}
````

## Define Entities

First the entities representing the database tables have to be defined, in this case `Abteilung` (Departement) and `Mitarbeiter` (Employee):

````java
@Entity(tableName = "abteilungen")
public class Abteilung {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
````

Entity classes are simple Java classes that define fields, getter- and setter-methods. Entities have to be annotated with the `@Entity` annotation. In the case of the `Mitarbeiter` Entity the annotation is also used to define a foreign key field.

````java
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
````

## Define Daos

Next we have to define available actions that can be executed using these entities on the database. Daos are defined as interfaces, Room handles the implementation.

````java
@Dao
public interface AbteilungDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAbteilungen(Abteilung...abteilungen);

    @Delete
    public void removeAbteilung(Abteilung abteilung);

    @Update
    public void updateAbteilungen(Abteilung...abteilungen);

    @Query("SELECT * FROM abteilungen")
    public List<Abteilung> getAllAbteilungen();

    @Query("SELECT * FROM abteilungen WHERE id = :id")
    public Abteilung getAbteilungById(int id);

}
````

Insert, update and delete are automatically handled by Room. To execute queries the SQL queries have to be manually defined.

````java
@Dao
public interface MitarbeiterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMitarbeiter(Mitarbeiter...mitarbeiter);

    @Delete
    public void removeMitarbeiter(Mitarbeiter mitarbeiter);

    @Update
    public void updateMitarbeiter(Mitarbeiter...mitarbeiter);

    @Query("SELECT * FROM mitarbeiter")
    public List<Mitarbeiter> getAllMitarbeiter();

    @Query("SELECT * FROM mitarbeiter WHERE id = :id")
    public Mitarbeiter getMitarbeiterById(int id);

    @Query("SELECT * FROM mitarbeiter WHERE abt_id = :abtId")
    public List<Mitarbeiter> getMitarbeiterOfAbteilung(int abtId);

}
````

## Define TypeConverters

Since the `Mitarbeiter` entity uses a date field which cannot be stored in a SQLite database the field hase to be converted to a UNIX timestamp and back. To automate this process we can write a TypeConverter:

````java
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
````

## Define the database

Now we can define the database using the entities, daos and type converters:

````java
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
````

The database is defined as an abstract class. To connect it with the beforehand created classes the `@Database` and `@TypeConverters` annotations are used. The database uses the Singleton pattern to make sure that only one instance is ever used.

## Access database

The database has to be accessed asynchronously. This is the AsyncTask used in the MainActivity to load all departements:

````java
class GetAbteilungenTask extends AsyncTask<Void, Void, List<Abteilung>> {

	@Override
    protected List<Abteilung> doInBackground(Void... voids) {
    	Database db = Database.getDatabase(MainActivity.this);
        return db.abteilungDao().getAllAbteilungen();
    }

    @Override
    protected void onPostExecute(List<Abteilung> abteilungen) {
    	setAbteilungen(abteilungen);
    }
}
````

The departements are loaded and passed to the set method of the activity.