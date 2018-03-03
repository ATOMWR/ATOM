package com.example.akav.atom.travel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.akav.atom.R;
/*TODO: apply listview adapter here.
two types of entries in form:
1.start date and end date will be different i.e. outside headquarters for more then one day
2.same start date and end date

Note:if there are multiple entries on single date,extra travelling hours shud  summed up together and entry in db will
     be this summed value of different entries on single date
*/
public class TravelForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_form);
    }
}
