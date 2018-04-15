package com.example.akav.atom;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.akav.atom.overtime.OvertimeFormListActivity;
import com.example.akav.atom.overtime.OvertimeSummaryListActivity;
import com.example.akav.atom.travel.TravelFormListActiviy;
import com.example.akav.atom.travel.TravelSummaryListActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ankit on 19-03-2018.
 */

public class ErrorCycleDateListAdapter extends ArrayAdapter<CycleDateObject> {

    public ErrorCycleDateListAdapter(Context context, ArrayList<CycleDateObject> errorCycleDateList){
        super(context, 0, errorCycleDateList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View dateListItemView = convertView;
        if(dateListItemView == null){
            dateListItemView = LayoutInflater.from(getContext()).inflate(R.layout.error_cycle_date_list_item, parent, false);
        }

        final CycleDateObject cycleDate = getItem(position);
        View formContainer = dateListItemView.findViewById(R.id.formContainer);

        Button gotoOtForms = (Button) dateListItemView.findViewById(R.id.ot_form_button);
        Button gotoTaForms = (Button) dateListItemView.findViewById(R.id.ta_form_button);

        gotoOtForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoOtFormList;

                if(cycleDate.getFlag() == 1){
                    gotoOtFormList = new Intent (getContext(), OvertimeFormListActivity.class);
                } else {
                    gotoOtFormList = new Intent (getContext(), OvertimeSummaryListActivity.class);
                }

                String errorCycleStartDate = cycleDate.getStartDate();
                String errorCycleEndDate = cycleDate.getEndDate();

                SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
                SimpleDateFormat endDateFormat = new SimpleDateFormat("dd - MM - yyyy");

                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = startDateFormat.parse(errorCycleStartDate);
                    endDate = endDateFormat.parse(errorCycleEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // Get Timestamp from date string
                Long startDateTimestamp = startDate.getTime() / 1000;
                Long endDateTimestamp = endDate.getTime() / 1000;

                String s = errorCycleStartDate.substring(10, 14) + "-" + errorCycleStartDate.substring(5, 7) + "-" + errorCycleStartDate.substring(0, 2);
                String e = errorCycleEndDate.substring(10, 14) + "-" + errorCycleEndDate.substring(5, 7) + "-" + errorCycleEndDate.substring(0, 2);

                gotoOtFormList.putExtra("startDate", startDateTimestamp.toString());
                gotoOtFormList.putExtra("endDate", endDateTimestamp.toString());
                gotoOtFormList.putExtra("isPreviousCycle", 2);
                gotoOtFormList.putExtra("strt", s);
                gotoOtFormList.putExtra("enddt", e);
                getContext().startActivity(gotoOtFormList);
            }
        });

        gotoTaForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoTaFormList;

                if(cycleDate.getFlag() == 1){
                    gotoTaFormList = new Intent (getContext(), TravelFormListActiviy.class);
                } else {
                    gotoTaFormList = new Intent (getContext(), TravelSummaryListActivity.class);
                }

                String errorCycleStartDate = cycleDate.getStartDate();
                String errorCycleEndDate = cycleDate.getEndDate();

                SimpleDateFormat startDateFormat = new SimpleDateFormat("dd - MM - yyyy");
                SimpleDateFormat endDateFormat = new SimpleDateFormat("dd - MM - yyyy");

                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = startDateFormat.parse(errorCycleStartDate);
                    endDate = endDateFormat.parse(errorCycleEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // Get Timestamp from date string
                Long startDateTimestamp = startDate.getTime() / 1000;
                Long endDateTimestamp = endDate.getTime() / 1000;

                String s = errorCycleStartDate.substring(10, 14) + "-" + errorCycleStartDate.substring(5, 7) + "-" + errorCycleStartDate.substring(0, 2);
                String e = errorCycleEndDate.substring(10, 14) + "-" + errorCycleEndDate.substring(5, 7) + "-" + errorCycleEndDate.substring(0, 2);

                gotoTaFormList.putExtra("startDate", startDateTimestamp.toString());
                gotoTaFormList.putExtra("endDate", endDateTimestamp.toString());
                gotoTaFormList.putExtra("isPreviousCycle", 2);
                gotoTaFormList.putExtra("strt", s);
                gotoTaFormList.putExtra("enddt", e);
                getContext().startActivity(gotoTaFormList);
            }
        });

        TextView startDate = (TextView) dateListItemView.findViewById(R.id.ot_prev_cycle_start);
        TextView endDate = (TextView)dateListItemView.findViewById(R.id.ot_prev_cycle_end);

        startDate.setText(cycleDate.getStartDate());
        endDate.setText(cycleDate.getEndDate());

        return dateListItemView;
    }

}
