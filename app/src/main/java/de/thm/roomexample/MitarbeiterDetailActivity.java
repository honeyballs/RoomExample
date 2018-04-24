package de.thm.roomexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import de.thm.roomexample.room.Database;
import de.thm.roomexample.room.Mitarbeiter;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

public class MitarbeiterDetailActivity extends AppCompatActivity {

    private TextView nameView, birthdayView, positionView, salaryView;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mitarbeiter_detail_layout);

        nameView = findViewById(R.id.nameText);
        birthdayView = findViewById(R.id.birthdayText);
        positionView = findViewById(R.id.positionText);
        salaryView = findViewById(R.id.salaryText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("mitarbeiter_id", 0);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetMitarbeiterTask().execute(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMitarbeiter(Mitarbeiter mitarbeiter) {
        nameView.setText("Name: " + mitarbeiter.getLastName() + ", " + mitarbeiter.getFirstName());
        positionView.setText("Position: " + mitarbeiter.getPosition());
        salaryView.setText("Gehalt: " + mitarbeiter.getSalary());
        Calendar c = Calendar.getInstance();
        c.setTime(mitarbeiter.getBirthday());
        birthdayView.setText("Geburtstag: " + c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + 1 + "." + c.get(Calendar.YEAR));
    }

    class GetMitarbeiterTask extends AsyncTask<Integer, Void, Mitarbeiter> {

        @Override
        protected Mitarbeiter doInBackground(Integer... integers) {
            Database db = Database.getDatabase(MitarbeiterDetailActivity.this);
            return db.mitarbeiterDao().getMitarbeiterById(integers[0]);
        }

        @Override
        protected void onPostExecute(Mitarbeiter mitarbeiter) {
            setMitarbeiter(mitarbeiter);
        }
    }
}
