package com.example.akav.atom.travel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akav.atom.R;
import com.example.akav.atom.overtime.OvertimeFormObject;

import java.util.ArrayList;

/**
 * Created by Ankit on 04-03-2018.
 */

public class TravelFormUserListAdapter extends ArrayAdapter<TravelFormObject> {

    public TravelFormUserListAdapter(Context context, ArrayList<TravelFormObject> forms){
        super(context, 0, forms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.travel_user_list_item, parent, false);
        }

        final TravelFormObject currentForm = getItem(position);

        View formContainer = listItemView.findViewById(R.id.travelUserFormContainer);

        TextView startStation = (TextView) listItemView.findViewById(R.id.start_station);
        TextView endStation  = (TextView) listItemView.findViewById(R.id.end_station);
        TextView reason = (TextView) listItemView.findViewById(R.id.reason_ta_form);

        startStation.setText(currentForm.getStartStation());
        endStation.setText(currentForm.getEndStation());
        reason.setText(currentForm.getReason());

        return listItemView;
    }

}
