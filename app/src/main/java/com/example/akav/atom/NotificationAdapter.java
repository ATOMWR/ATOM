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
    private String[] textViewValues,ftypes;



    public NotificationAdapter(Context context, String[] textViewValues ,String[] ftypes) {
        this.context = context;
        this.textViewValues = textViewValues;
        this.ftypes=ftypes;


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
            String ss=ftypes[position]+":"+textViewValues[position];
            textView.setText(ss);

          //  textView.setText(ftypes[position]+":"+textViewValues[position]);

          /*  if(textView.getText().toString().equals(" 3 ")||textView.getText().toString().equals(" 8 "))
                gridView.setBackgroundColor(Color.parseColor("#E98D71"));
            else if(textView.getText().toString().equals(" 6 ")||textView.getText().toString().equals(" 9 "))
                gridView.setBackgroundColor(Color.parseColor("#ADDF41"));
            else if(textView.getText().toString().equals(" 20 ")||textView.getText().toString().equals(" 15 "))
                gridView.setBackgroundColor(Color.parseColor("#E3E358"));
             else*/
            int a=textViewValues.length;
            for (int i = 0; i < a; i++) {
                if(textViewValues[i].equals("nodate")) {
                    textView.setText("no notifications");
                   // break;
                }


            }






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
