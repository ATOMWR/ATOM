package com.example.akav.atom;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by AKAV on 07-Mar-18.
 */

public class NotificationAdapter extends BaseAdapter {


    private Context context;
    private String[] textViewValues;


    public NotificationAdapter(Context context, String[] textViewValues ) {
        this.context = context;
        this.textViewValues = textViewValues;


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        gridView = new View(context);
        gridView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 250));


        if (convertView == null) {




            // get layout from item.xml
            gridView = inflater.inflate(R.layout.notificationitem, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.not_textid);
            textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);


            textView.setText(textViewValues[position]);

          /*  if(textView.getText().toString().equals(" 3 ")||textView.getText().toString().equals(" 8 "))
                gridView.setBackgroundColor(Color.parseColor("#E98D71"));
            else if(textView.getText().toString().equals(" 6 ")||textView.getText().toString().equals(" 9 "))
                gridView.setBackgroundColor(Color.parseColor("#ADDF41"));
            else if(textView.getText().toString().equals(" 20 ")||textView.getText().toString().equals(" 15 "))
                gridView.setBackgroundColor(Color.parseColor("#E3E358"));
             else
            int a=js.length;
            for (int i = 0; i < a; i++) {
                if(js[i].equals("nodate")) {
                    gridView.setBackgroundColor(Color.parseColor("#C8EFE0"));
                    gridView.setTag(new String("no activity"));
                    break;
                }
                else if (js[i].equals(textViewValues[position])) {
                    if (ji[i] == 0) {
                        gridView.setBackgroundColor(Color.parseColor("#E3E358"));
                        gridView.setTag(new String("ot filled"));
                    }
                    else if (ji[i] == 1){
                        gridView.setBackgroundColor(Color.parseColor("#ADDF41"));
                        gridView.setTag(new String("ot verified"));
                    }
                    else if (ji[i] == 2){
                        gridView.setBackgroundColor(Color.parseColor("#E98D71"));
                        gridView.setTag(new String("ot notify"));
                    }
                    break;
                } else {

                    gridView.setBackgroundColor(Color.parseColor("#C8EFE0"));
                    gridView.setTag(new String("no activity"));
                }
            }*/






        } else {


            gridView = (View) convertView;

        }
       /* if(js==1)
         gridView.setBackgroundColor(Color.parseColor("#C8EFE0"));
        else
            gridView.setBackgroundColor(Color.parseColor("#E3E358"));*/
        return gridView;
    }


    @Override
    public int getCount() {
        return textViewValues.length;
    }

    @Override
    public Object getItem(int position) {
        return textViewValues[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
