package com.example.akav.atom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ankit on 01-02-2018.
 */

public class OvertimeFormListAdapter extends ArrayAdapter<OvertimeFormObject> {

    public OvertimeFormListAdapter(Context context, ArrayList<OvertimeFormObject> forms){
        super(context, 0, forms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.overtime_list_item, parent, false);
        }

        OvertimeFormObject currentForm = getItem(position);

        View listItem = listItemView.findViewById(R.id.formContainer);

        TextView nameOnForm = (TextView) listItemView.findViewById(R.id.name_on_form);
        TextView date = (TextView) listItemView.findViewById(R.id.form_filled_date);
        TextView shift = (TextView) listItemView.findViewById(R.id.shift_on_form);
        TextView platformNo = (TextView) listItemView.findViewById(R.id.platform_no);
        TextView actualStart = (TextView) listItemView.findViewById(R.id.actual_start_time);
        TextView actualEnd = (TextView) listItemView.findViewById(R.id.actual_end_time);
        TextView extraHours = (TextView) listItemView.findViewById(R.id.extra_hours);

        nameOnForm.setText(currentForm.getName());
        date.setText(currentForm.getDate());
        shift.setText(currentForm.getShift());
        platformNo.setText(currentForm.getPlatformNumber());
        actualStart.setText(currentForm.getActualStart());
        actualEnd.setText(currentForm.getActualEnd());
        extraHours.setText(currentForm.getExtraHours());

        // listItem.setBackgroundColor(color);

        return listItemView;
    }
}
