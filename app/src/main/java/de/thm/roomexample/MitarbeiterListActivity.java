package de.thm.roomexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.roomexample.room.Database;
import de.thm.roomexample.room.Mitarbeiter;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

public class MitarbeiterListActivity extends AppCompatActivity {

    private ListView listView;
    private List<Mitarbeiter> mitarbeiter;
    private MitarbeiterAdapter adapter;
    private int abteilungsId = 0;
    private String abteilungsName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);
        listView = findViewById(R.id.mainListView);
        mitarbeiter = new ArrayList<>();
        adapter = new MitarbeiterAdapter(this, mitarbeiter);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new MitarbeiterDeleteListener());

        //Get the selected id of the Abteilung
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.size() > 0) {
            this.abteilungsId = extras.getInt("abteilungs_id", 0);
            this.abteilungsName = extras.getString("abteilungs_name", "");
            setTitle(abteilungsName);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent intent = new Intent(this, AddMitarbeiterActivity.class);
                intent.putExtra("abteilungs_id", abteilungsId);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        new GetMitarbeiterTask().execute(abteilungsId);
    }

    private void setMitarbeiter(List<Mitarbeiter> mitarbeiter) {
        this.mitarbeiter.clear();
        this.mitarbeiter.addAll(mitarbeiter);
        adapter.notifyDataSetChanged();
    }

    class MitarbeiterDeleteListener implements ListView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Mitarbeiter selectedMitarbeiter = mitarbeiter.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MitarbeiterListActivity.this);
            dialog.setTitle(selectedMitarbeiter.getFirstName() + " " + selectedMitarbeiter.getLastName() + " löschen?");
            dialog.setMessage("Möchten Sie " + selectedMitarbeiter.getFirstName() + " " + selectedMitarbeiter.getLastName() + " wirklich löschen?");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteMitarbeiterTask().execute(selectedMitarbeiter);
                }
            });
            dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
    }

    class GetMitarbeiterTask extends AsyncTask<Integer, Void, List<Mitarbeiter>> {

        @Override
        protected List<Mitarbeiter> doInBackground(Integer... ids) {
            Database db = Database.getDatabase(MitarbeiterListActivity.this);
            return db.mitarbeiterDao().getMitarbeiterOfAbteilung(ids[0]);
        }

        @Override
        protected void onPostExecute(List<Mitarbeiter> mitarbeiter) {
            setMitarbeiter(mitarbeiter);
        }
    }

    class DeleteMitarbeiterTask extends AsyncTask<Mitarbeiter, Void, Void> {

        @Override
        protected Void doInBackground(Mitarbeiter... mitarbeiter) {
            Database db = Database.getDatabase(MitarbeiterListActivity.this);
            db.mitarbeiterDao().removeMitarbeiter(mitarbeiter[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refresh();
        }
    }


}
