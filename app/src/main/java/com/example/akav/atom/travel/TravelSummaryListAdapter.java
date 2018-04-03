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

import java.util.ArrayList;

public class TravelSummaryListAdapter extends ArrayAdapter<TravelSummaryObject> {

    public TravelSummaryListAdapter(Context context, ArrayList<TravelSummaryObject> forms) {
        super(context, 0, forms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.travel_summary_list_item, parent, false);
        }

        final TravelSummaryObject currentForm = getItem(position);

        View formContainer = listItemView.findViewById(R.id.formContainer);

        TextView nameOnForm = (TextView) listItemView.findViewById(R.id.name_on_form);
        TextView category = (TextView) listItemView.findViewById(R.id.user_category);
        TextView totalTravels = (TextView) listItemView.findViewById(R.id.total_travels);
        TextView totalTravelHours = (TextView) listItemView.findViewById(R.id.total_travel_hours);
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
        totalTravels.setText(currentForm.getTotalTravels());
        totalTravelHours.setText(currentForm.getTotalTravelHours());
        reason.setText(currentForm.getReason());
        verificationStatus.setImageResource(currentForm.getVerificationStatusImageId());

        return listItemView;

    }
}
