package com.example.akav.atom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ankit on 09-02-2018.
 */

public class previousCycleDateListAdapter extends ArrayAdapter<OtCycleDateObject> {

    public previousCycleDateListAdapter(Context context, ArrayList<OtCycleDateObject> previosCycleDateList){
        super(context, 0, previosCycleDateList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View dateListItemView = convertView;
        if(dateListItemView == null){
            dateListItemView = LayoutInflater.from(getContext()).inflate(R.layout.previous_cycle_date_list_item, parent, false);
        }

        OtCycleDateObject cycleDate = getItem(position);

        TextView startDate = (TextView) dateListItemView.findViewById(R.id.ot_prev_cycle_start);
        TextView endDate = (TextView)dateListItemView.findViewById(R.id.ot_prev_cycle_end);

        startDate.setText(cycleDate.getStartDate());
        endDate.setText(cycleDate.getEndDate());

        return dateListItemView;
    }
}
