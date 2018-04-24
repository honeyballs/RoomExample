package de.thm.roomexample;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import de.thm.roomexample.room.Database;
import de.thm.roomexample.room.Mitarbeiter;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

public class AddMitarbeiterActivity extends AppCompatActivity {

    private EditText firstNameField, lastNameField, positionField, salaryField;
    private NumberPicker dayPicker, monthPicker, yearPicker;
    private int abteilungsId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_mitarbeiter_layout);

        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        positionField = findViewById(R.id.positionField);
        salaryField = findViewById(R.id.salaryField);

        dayPicker = findViewById(R.id.dayPicker);
        monthPicker = findViewById(R.id.monthPicker);
        yearPicker = findViewById(R.id.yearPicker);

        initPickers();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            abteilungsId = extras.getInt("abteilungs_id", 0);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.confirm_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_add:
                addMitarbeiter();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initPickers() {
        Calendar calender = Calendar.getInstance();
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(calender.get(Calendar.DAY_OF_MONTH));

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(calender.get(Calendar.MONTH)+1);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(calender.get(Calendar.YEAR));
        yearPicker.setValue(calender.get(Calendar.YEAR));
    }

    private void addMitarbeiter() {
        if (firstNameField.length() > 0 && lastNameField.length() > 0 && positionField.length() > 0 && salaryField.length() > 0) {
            Date birthday = new Date(yearPicker.getValue(), monthPicker.getValue() - 1, dayPicker.getValue());
            Mitarbeiter mitarbeiter = new Mitarbeiter();
            mitarbeiter.setAbtId(abteilungsId);
            mitarbeiter.setFirstName(firstNameField.getText().toString());
            mitarbeiter.setLastName(lastNameField.getText().toString());
            mitarbeiter.setPosition(positionField.getText().toString());
            mitarbeiter.setSalary(Integer.parseInt(salaryField.getText().toString()));
            mitarbeiter.setBirthday(birthday);
            new AddMitarbeiterTask().execute(mitarbeiter);
        } else {
            Toast.makeText(this, "Bitte füllen Sie alle Felder aus.", Toast.LENGTH_SHORT).show();
        }
    }

    class AddMitarbeiterTask extends AsyncTask<Mitarbeiter, Void, Void> {

        @Override
        protected Void doInBackground(Mitarbeiter... mitarbeiter) {
            Database db = Database.getDatabase(AddMitarbeiterActivity.this);
            db.mitarbeiterDao().insertMitarbeiter(mitarbeiter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            onBackPressed();
        }
    }

}
