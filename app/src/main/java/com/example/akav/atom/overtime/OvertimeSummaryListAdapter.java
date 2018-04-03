package com.example.akav.atom.overtime;

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

import java.util.ArrayList;

public class OvertimeSummaryListAdapter extends ArrayAdapter<OvertimeSummaryObject> {

    public OvertimeSummaryListAdapter(Context context, ArrayList<OvertimeSummaryObject> forms){
        super(context, 0, forms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.overtime_summary_list_item, parent, false);
        }

        final OvertimeSummaryObject currentForm = getItem(position);

        View formContainer = listItemView.findViewById(R.id.formContainer);

        TextView nameOnForm = (TextView) listItemView.findViewById(R.id.name_on_form);
        TextView category = (TextView) listItemView.findViewById(R.id.user_category);
        TextView eightHourDuty = (TextView) listItemView.findViewById(R.id.eight_hours_duty);
        TextView sixHourDuty = (TextView) listItemView.findViewById(R.id.six_hours_duty);
        TextView totalRosteredDutyHours = (TextView) listItemView.findViewById(R.id.rostered_duty_hours);
        TextView totalActualDutyHours = (TextView) listItemView.findViewById(R.id.actual_duty_hours);
        TextView extraDutyHours = (TextView) listItemView.findViewById(R.id.extra_duty_hours);
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
        category.setText(currentForm.getCategory());
        eightHourDuty.setText(currentForm.getTotalEightHours() + " Hrs.");
        sixHourDuty.setText(currentForm.getTotalSixHours() + " Hrs.");
        totalRosteredDutyHours.setText(currentForm.getTotalRosteredDutyHours() + " Hrs."
                                        + " (" + currentForm.getTotalEightHours() + "Hrs. + " + currentForm.getTotalSixHours() + " Hrs.)");
        totalActualDutyHours.setText(currentForm.getTotalActualDutyHours() + " Hrs.");
        extraDutyHours.setText(currentForm.getExtraDutyHours() + " Hrs.");
        reason.setText(currentForm.getReason());
        verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());

        return listItemView;
    }
}
