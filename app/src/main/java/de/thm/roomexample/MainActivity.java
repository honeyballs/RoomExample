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

import de.thm.roomexample.room.Abteilung;
import de.thm.roomexample.room.Database;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Abteilung> abteilungen;
    private AbteilungsAdapter abteilungsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);
        listView = findViewById(R.id.mainListView);
        listView.setOnItemLongClickListener(new AbteilungsDeleteListener());
        abteilungen = new ArrayList<>();
        abteilungsAdapter = new AbteilungsAdapter(this, abteilungen);
        listView.setAdapter(abteilungsAdapter);

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
                showAddAbteilungDialog();
                return true;
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
        new GetAbteilungenTask().execute();
    }

    private void setAbteilungen(List<Abteilung> abteilungen) {
        this.abteilungen.clear();
        this.abteilungen.addAll(abteilungen);
        abteilungsAdapter.notifyDataSetChanged();
    }

    private void showAddAbteilungDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Abteilung hinzufügen");
        dialog.setMessage("Geben Sie den Namen der Abteilung ein.");

        //Create an input field for the abteilungs name
        final EditText input = new EditText(this);
        input.setHint("Abteilungsname");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(params);

        dialog.setView(input);

        dialog.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (name.length() > 0) {
                    Abteilung abteilung = new Abteilung();
                    abteilung.setName(name);
                    new AddAbteilungTask().execute(abteilung);
                } else {
                    Toast.makeText(MainActivity.this, "Bitte geben Sie einen Namen ein", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class AbteilungsClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, MitarbeiterListActivity.class);
            intent.putExtra("abteilungs_id", abteilungen.get(position).getId());
            intent.putExtra("abteilungs_name", abteilungen.get(position).getName());
            startActivity(intent);
        }
    }

    class AbteilungsDeleteListener implements ListView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Abteilung abteilung = abteilungen.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(abteilung.getName() +" löschen?");
            dialog.setMessage("Möchten Sie " + abteilung.getName() + " wirklich löschen?");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteAbteilungTask().execute(abteilung);
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

    class AddAbteilungTask extends AsyncTask<Abteilung, Void, Void> {


        @Override
        protected Void doInBackground(Abteilung... abteilungen) {
            Database db = Database.getDatabase(MainActivity.this);
            db.abteilungDao().insertAbteilungen(abteilungen);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refresh();
        }
    }

    class DeleteAbteilungTask extends AsyncTask<Abteilung, Void, Void> {

        @Override
        protected Void doInBackground(Abteilung... abteilungen) {
            Database db = Database.getDatabase(MainActivity.this);
            db.abteilungDao().removeAbteilung(abteilungen[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refresh();
        }
    }
}
