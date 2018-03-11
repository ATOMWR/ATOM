package com.example.akav.atom.travel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Created by Ankit on 11-03-2018.
 */

public class TravelFormListAdapter extends ArrayAdapter<TravelFormObject> {

    public TravelFormListAdapter(Context context, ArrayList<TravelFormObject> forms){
        super(context, 0, forms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.travel_list_item, parent, false);
        }

        final TravelFormObject currentForm = getItem(position);

        View formContainer = listItemView.findViewById(R.id.formContainer);

        TextView nameOnForm = (TextView) listItemView.findViewById(R.id.name_on_form);
        TextView date = (TextView) listItemView.findViewById(R.id.form_filled_date);
        TextView startStation = (TextView) listItemView.findViewById(R.id.from_station);
        TextView endStation = (TextView) listItemView.findViewById(R.id.to_station);
        TextView startTime = (TextView) listItemView.findViewById(R.id.actual_start_time);
        TextView endTime = (TextView) listItemView.findViewById(R.id.actual_end_time);
        TextView extraHours = (TextView) listItemView.findViewById(R.id.extra_hours);
        TextView reason = (TextView) listItemView.findViewById(R.id.reason_on_form);

        Button verify = (Button) formContainer.findViewById(R.id.verify_form);
        Button undo = (Button) formContainer.findViewById(R.id.undo_form_action);
        Button notifyUser = (Button) formContainer.findViewById(R.id.notify_user);

        final ImageView verificationStatus = (ImageView) formContainer.findViewById(R.id.verification_status);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentForm.setInterVerification(1);
                verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentForm.setInterVerification(0);
                verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());
            }
        });

        notifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentForm.setInterVerification(2);
                verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());
            }
        });

        nameOnForm.setText(currentForm.getName());
        date.setText(currentForm.getDateOfTravel());
        startStation.setText(currentForm.getStartStation());
        endStation.setText(currentForm.getEndStation());
        startTime.setText(currentForm.getStartTime());
        endTime.setText(currentForm.getEndTime());
        extraHours.setText(currentForm.getExtraHours());
        reason.setText(currentForm.getReason());
        verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());

        // formContainer.setBackgroundColor(color);

        return listItemView;
    }
}
