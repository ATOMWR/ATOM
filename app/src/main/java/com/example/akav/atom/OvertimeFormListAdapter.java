package com.example.akav.atom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        final OvertimeFormObject currentForm = getItem(position);

        View formContainer = listItemView.findViewById(R.id.formContainer);

        TextView nameOnForm = (TextView) listItemView.findViewById(R.id.name_on_form);
        TextView date = (TextView) listItemView.findViewById(R.id.form_filled_date);
        TextView shift = (TextView) listItemView.findViewById(R.id.shift_on_form);
        TextView actualStart = (TextView) listItemView.findViewById(R.id.actual_start_time);
        TextView actualEnd = (TextView) listItemView.findViewById(R.id.actual_end_time);
        TextView extraHours = (TextView) listItemView.findViewById(R.id.extra_hours);
        TextView reason = (TextView) listItemView.findViewById(R.id.reason_on_form);
        TextView shiftTime = (TextView) listItemView.findViewById(R.id.shift_time);

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
        date.setText(currentForm.getDate());
        shift.setText(currentForm.getShift());
        actualStart.setText(currentForm.getActualStart());
        actualEnd.setText(currentForm.getActualEnd());
        extraHours.setText(currentForm.getExtraHours());
        reason.setText(currentForm.getReason());
        verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());

        switch (currentForm.getShift()) {

            case "Morning":
                shiftTime.setText(" (06:00 TO 14:00)");
                break;

            case "Evening":
                shiftTime.setText(" (14:00 TO 22:00)");
                break;

            case "Night":
                shiftTime.setText(" (22:00 TO 06:00)");
                break;

            default:
                shiftTime.setText(" (00:00 TO 00:00)");
        }

        // formContainer.setBackgroundColor(color);

        return listItemView;
    }
}
