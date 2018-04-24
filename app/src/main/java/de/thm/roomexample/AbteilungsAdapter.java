package de.thm.roomexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.thm.roomexample.room.Abteilung;

/**
 * Created by Yannick Bals on 24.04.2018.
 */

public class AbteilungsAdapter extends ArrayAdapter<Abteilung> {

    public AbteilungsAdapter(Context context, List<Abteilung> abteilungen) {
        super(context, 0, abteilungen);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Abteilung abteilung = getItem(position);

        //If no view is reused, create a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        //Set the data
        TextView nameView = convertView.findViewById(android.R.id.text1);
        nameView.setText(abteilung.getName());

        return convertView;
    }
}
