package com.example.akav.atom.travel;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.akav.atom.R;

/**
 * Created by AKAV on 03-Mar-18.
 */

public class TextViewAdapterTA  extends BaseAdapter{
    private Context context;
    private final String[] textViewValues;
    private String[] js;
    private int[] ji;


    public TextViewAdapterTA(Context context, String[] textViewValues,String[] s,int[] a) {
        this.context = context;
        this.textViewValues = textViewValues;
        js=s;
        ji=a;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        gridView = new View(context);
        gridView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 250));


        if (convertView == null) {




            // get layout from item.xml
            gridView = inflater.inflate(R.layout.item, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.textView3);
            textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);


            textView.setText(textViewValues[position].substring(8,10));

          /*  if(textView.getText().toString().equals(" 3 ")||textView.getText().toString().equals(" 8 "))
                gridView.setBackgroundColor(Color.parseColor("#E98D71"));
            else if(textView.getText().toString().equals(" 6 ")||textView.getText().toString().equals(" 9 "))
                gridView.setBackgroundColor(Color.parseColor("#ADDF41"));
            else if(textView.getText().toString().equals(" 20 ")||textView.getText().toString().equals(" 15 "))
                gridView.setBackgroundColor(Color.parseColor("#E3E358"));
             else*/
          for(int i=0;i<js.length;i++){
              for(int j=i+1;j<js.length;j++){
                  if(js[i].equals(js[j])){
                      if(ji[i]==2||ji[j]==2){
                          ji[i]=2;
                          ji[j]=2;
                      }
                  }
              }
          }

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
