package com.example.akav.atom.travel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.akav.atom.MainActivity;
import com.example.akav.atom.R;
import com.example.akav.atom.overtime.OvertimeForm;
import com.example.akav.atom.overtime.OvertimeFormListActivity;
import com.example.akav.atom.overtime.OvertimeFormObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*TODO: apply listview adapter here.
two types of entries in form:
1.start date and end date will be different i.e. outside headquarters for more then one day
2.same start date and end date

Note:if there are multiple entries on single date,extra travelling hours shud  summed up together and entry in db will
     be this summed value of different entries on single date
*/
public class TravelForm extends AppCompatActivity {

    private EditText tspNumberEditText;
    private EditText startTimeEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private EditText endTimeEditText;
    private EditText startStationEditText;
    private EditText endStationEditText;
    private EditText reasonEditText;

    private Button selectStartDate;
    private Button selectEndDate;
    private Button selectStartTime;
    private Button selectEndTime;
    private Button submitForm;

    private ProgressBar progressBar;

    private String dateOfTravel;
    private String pfNumber;
    private String userID;
    private String tspNumber;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String startStation;
    private String endStation;
    private String reason;
    private String extraHours;

    private final String FILL_TA_FORM_URL = "http://atomwrapp.dx.am/taFill.php";

    private Integer percentageCategory;
    private Integer interVerification;
    private Integer finalVerification;

    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;

    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;

    private Integer startDay;
    private Integer startMonth;
    private Integer endDay;
    private Integer endMonth;

    private Long startDateTimestamp;
    private Long endDateTimestamp;

    ArrayList<TravelFormObject> dataToInsertArrayList;
    private JSONObject jsonToSend;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_form);

        Intent intent = getIntent();

        dateOfTravel = intent.getStringExtra("currdate");
        userID = intent.getStringExtra("userID");

        dataToInsertArrayList = new ArrayList<>();

        selectStartDate = (Button) findViewById(R.id.select_start_date);
        selectEndDate = (Button) findViewById(R.id.select_end_date);
        selectStartTime = (Button) findViewById(R.id.select_start_time);
        selectEndTime = (Button) findViewById(R.id.select_end_time);

        tspNumberEditText = (EditText) findViewById(R.id.tsp_number);
        startTimeEditText = (EditText) findViewById(R.id.ta_start_time);
        startDateEditText = (EditText) findViewById(R.id.ta_start_date);
        endDateEditText = (EditText) findViewById(R.id.ta_end_date);
        endTimeEditText = (EditText) findViewById(R.id.ta_end_time);
        startStationEditText = (EditText) findViewById(R.id.ta_start_station);
        endStationEditText = (EditText) findViewById(R.id.ta_end_station);
        reasonEditText = (EditText) findViewById(R.id.ta_reason);

        submitForm = (Button) findViewById(R.id.ta_submit_form);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TravelForm.this, startDateListener, year, month, day);
                datePickerDialog.show();
            }
        });

        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TravelForm.this, endDateListener, year, month, day);
                datePickerDialog.show();
            }
        });

        selectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog datePickerDialog = new TimePickerDialog(TravelForm.this, startTimeListener, hour, minute, true);
                datePickerDialog.show();
            }
        });

        selectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog datePickerDialog = new TimePickerDialog(TravelForm.this, endTimeListener, hour, minute, true);
                datePickerDialog.show();
            }
        });

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get data to send
                pfNumber = "null";
                tspNumber = tspNumberEditText.getText().toString();
                startTime = startTimeEditText.getText().toString();
                endTime = endTimeEditText.getText().toString();
                startStation = startStationEditText.getText().toString();
                endStation = endStationEditText.getText().toString();
                reason = reasonEditText.getText().toString();

                interVerification = 0;
                finalVerification = 0;

                String startDateString = startDate + " " + startTime + ":00";
                String endDateString = endDate + " " + endTime + ":00";

                Long timeDifference;
                Long hourDifference;
                Long minuteDifference;

                Integer dateDifference = endDay - startDay;
                Integer monthDifference = endMonth - startMonth;

                if ((dateDifference == 0) && (monthDifference == 0)) {
                    // Same Date
                    timeDifference = getTimeDifference(startDateString, endDateString);
                    hourDifference = timeDifference / 1000 / 60 / 60;
                    minuteDifference = ((timeDifference / 1000 / 60) % 60);

                    percentageCategory = getPercentageCategory(hourDifference);
                    extraHours = hourDifference + ":" + minuteDifference + ":00";

                    dateOfTravel = startDate;

                    dataToInsertArrayList.add(new TravelFormObject(dateOfTravel, pfNumber, userID, tspNumber, startDate,
                            endDate, startTime, endTime, startStation, endStation, extraHours, reason,
                            percentageCategory, interVerification, finalVerification));

                } else {
                    // Different dates
                    // Case 1: Difference of 1 day, Same month
                    if ((dateDifference == 1) && (monthDifference == 0)) {
                        // First Day TA
                        String tempEndTime = "23:59:99";
                        String tempEndDate = startDate;
                        String tempEndDateString = tempEndDate + " " + tempEndTime;

                        timeDifference = getTimeDifference(startDateString, tempEndDateString);
                        hourDifference = timeDifference / 1000 / 60 / 60;
                        minuteDifference = ((timeDifference / 1000 / 60) % 60);

                        percentageCategory = getPercentageCategory(hourDifference);
                        extraHours = hourDifference + ":" + minuteDifference + ":00";
                        dateOfTravel = startDate;

                        dataToInsertArrayList.add(new TravelFormObject(dateOfTravel, pfNumber, userID, tspNumber, startDate,
                                tempEndDate, startTime, tempEndTime, startStation, endStation, extraHours, reason,
                                percentageCategory, interVerification, finalVerification));

                        // Second Day TA
                        startDate = endDate;
                        startTime = "00:00:00";
                        String tempStartDateString = startDate + " " + startTime;

                        timeDifference = getTimeDifference(tempStartDateString, endDateString);
                        hourDifference = timeDifference / 1000 / 60 / 60;
                        minuteDifference = ((timeDifference / 1000 / 60) % 60);

                        percentageCategory = getPercentageCategory(hourDifference);
                        extraHours = hourDifference + ":" + minuteDifference + ":00";
                        dateOfTravel = startDate;
                        dataToInsertArrayList.add(new TravelFormObject(dateOfTravel, pfNumber, userID, tspNumber, startDate,
                                endDate, startTime, endTime, startStation, endStation, extraHours, reason,
                                percentageCategory, interVerification, finalVerification));
                    }

                    // Case 2: Difference more than One Day, Same Month
                    if ((dateDifference > 1) && (monthDifference == 0)) {
                        // First Day TA
                        String tempEndTime = "23:59:99";
                        String tempEndDate = startDate;
                        String tempEndDateString = tempEndDate + " " + tempEndTime;

                        timeDifference = getTimeDifference(startDateString, tempEndDateString);
                        hourDifference = timeDifference / 1000 / 60 / 60;
                        minuteDifference = ((timeDifference / 1000 / 60) % 60);

                        percentageCategory = getPercentageCategory(hourDifference);
                        extraHours = hourDifference + ":" + minuteDifference + ":00";
                        dateOfTravel = startDate;

                        dataToInsertArrayList.add(new TravelFormObject(dateOfTravel, pfNumber, userID, tspNumber, startDate,
                                tempEndDate, startTime, tempEndTime, startStation, endStation, extraHours, reason,
                                percentageCategory, interVerification, finalVerification));

                        // Intermediate days
                        Integer numberOfDays = 1;

                        while (numberOfDays < dateDifference) {
                            String intermediateStartDate = year + "-" + (startMonth) + "-" + (startDay + numberOfDays);
                            String intermediateStartTime = "00:00:00";
                            String intermediateEndDate = intermediateStartDate;
                            String intermediateEndTime = "23:59:99";

                            String intermediateStartDateString = intermediateStartDate + " " + intermediateStartTime;
                            String intermediateEndDateString = intermediateEndDate + " " + intermediateEndTime;

                            timeDifference = getTimeDifference(intermediateStartDateString, intermediateEndDateString);
                            hourDifference = timeDifference / 1000 / 60 / 60;
                            minuteDifference = ((timeDifference / 1000 / 60) % 60);

                            percentageCategory = getPercentageCategory(hourDifference);
                            extraHours = hourDifference + ":" + minuteDifference + ":00";
                            dateOfTravel = intermediateStartDate;

                            dataToInsertArrayList.add(new TravelFormObject(dateOfTravel, pfNumber, userID, tspNumber, intermediateStartDate,
                                    intermediateEndDate, intermediateStartTime, intermediateEndTime, startStation, endStation, extraHours,
                                    reason, percentageCategory, interVerification, finalVerification));

                            numberOfDays += 1;
                        }

                        // Last Day TA
                        startDate = endDate;
                        startTime = "00:00:00";
                        String tempStartDateString = startDate + " " + startTime;

                        timeDifference = getTimeDifference(tempStartDateString, endDateString);
                        hourDifference = timeDifference / 1000 / 60 / 60;
                        minuteDifference = ((timeDifference / 1000 / 60) % 60);

                        percentageCategory = getPercentageCategory(hourDifference);
                        extraHours = hourDifference + ":" + minuteDifference + ":00";
                        dateOfTravel = startDate;

                        dataToInsertArrayList.add(new TravelFormObject(dateOfTravel, pfNumber, userID, tspNumber, startDate,
                                endDate, startTime, endTime, startStation, endStation, extraHours, reason,
                                percentageCategory, interVerification, finalVerification));
                    }

                }

                Log.i(TravelForm.class.getName(), dataToInsertArrayList.size() + "");

                for (TravelFormObject travelFormObject : dataToInsertArrayList) {
                    Log.i(TravelForm.class.getName(), "date of travel " + travelFormObject.getDateOfTravel().toString());
                    Log.i(TravelForm.class.getName(), "start time " + travelFormObject.getStartTime().toString());
                    Log.i(TravelForm.class.getName(), "end time " + travelFormObject.getEndTime().toString());
                    Log.i(TravelForm.class.getName(), "extra hours " + travelFormObject.getExtraHours().toString());
                }

                jsonToSend = ArrayListToJson(dataToInsertArrayList);

                Log.i(TravelForm.class.getName(), "JSON to send: " + jsonToSend.toString());


                if (isOnline()) {
                    // Start the AsyncTask
                    progressBar.setVisibility(View.VISIBLE);
                    MainAsyncTask task = new MainAsyncTask();
                    task.execute(FILL_TA_FORM_URL);
                } else {
                    Toast.makeText(TravelForm.this, "NO Internet Connection, Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private DatePickerDialog.OnDateSetListener startDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    String dateString = year + "-" + (month + 1) + "-" + date;

                    String tempDate = date + "-" + (month + 1) + "-" + year;

                    startMonth = month + 1;
                    startDay = date;
                    startDate = dateString;

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    Date selectedDate = null;
                    try {
                        selectedDate = formatter.parse(tempDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    startDateEditText.setText(formatter.format(selectedDate));
                }
            };

    private DatePickerDialog.OnDateSetListener endDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    String dateString = year + "-" + (month + 1) + "-" + date;

                    String tempDate = date + "-" + (month + 1) + "-" + year;

                    endMonth = month + 1;
                    endDay = date;
                    endDate = dateString;

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    Date selectedDate = null;
                    try {
                        selectedDate = formatter.parse(tempDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateEditText.setText(formatter.format(selectedDate));
                }
            };

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            startTime = selectedHour + ":" + selectedMinute;

            startHour = selectedHour;
            startMinute = selectedMinute;

            startTimeEditText.setText(startTime);
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            endTime = selectedHour + ":" + selectedMinute;

            endHour = selectedHour;
            endMinute = selectedMinute;

            endTimeEditText.setText(endTime);
        }
    };

    private Long getTimeDifference(String startDateString, String endDateString) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date sDate = null;
        try {
            sDate = formatter1.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDateTimestamp = sDate.getTime();
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date eDate = null;
        try {
            eDate = formatter2.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endDateTimestamp = eDate.getTime();

        return endDateTimestamp - startDateTimestamp;
    }

    private Integer getPercentageCategory(Long hourDifference) {
        if (hourDifference < 6) {
            return 30;
        } else if ((hourDifference > 6) & (hourDifference < 12)) {
            return 70;
        } else {
            return 100;
        }
    }


    // Generate a JSON object to send via http request
    private JSONObject ArrayListToJson(ArrayList<TravelFormObject> formList) {

        JSONObject formsListJsonObject = new JSONObject();
        JSONArray formsArray = new JSONArray();

        for (int index = 0; index < formList.size(); index++) {

            JSONObject singleForm = new JSONObject();
            try {
                singleForm.put("date", formList.get(index).getDateOfTravel());
                singleForm.put("pfno", formList.get(index).getPfNumber());
                singleForm.put("name", formList.get(index).getName());
                singleForm.put("tspNo", formList.get(index).getTspNumber());
                singleForm.put("startTime", formList.get(index).getStartTime());
                singleForm.put("startStation", formList.get(index).getStartStation());
                singleForm.put("endTime", formList.get(index).getEndTime());
                singleForm.put("endStation", formList.get(index).getEndStation());
                singleForm.put("extraHours", formList.get(index).getExtraHours());
                singleForm.put("reason", formList.get(index).getReason());
                singleForm.put("percentageCategory", formList.get(index).getPercentageCategory());
                singleForm.put("interVerification", formList.get(index).getInterVerification());
                singleForm.put("finalVerification", formList.get(index).getFinalVerification());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            formsArray.put(singleForm);
        }

        try {
            formsListJsonObject.put("forms", formsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return formsListJsonObject;
    }


    /**
     * Background thread logic
     */

    private String fillTaForm(String url) {

        URL finalInsertUrl = null;

        try {
            finalInsertUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
            ;
        }

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(finalInsertUrl);
            if (jsonResponse == null) {
                return null;
            }
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
        }

        String result = null;
        try {
            JSONObject root = new JSONObject(jsonResponse);
            result = root.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonToSend.toString());
            out.close();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (SocketTimeoutException s) {
            s.printStackTrace();
            return null;
        } catch (UnknownHostException u) {
            u.printStackTrace();
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), "Problem retrieving JSON.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert JSON to String.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private class MainAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = fillTaForm(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to the Internet, Try again", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setGravity(Gravity.CENTER);
                toast.show();
            } else {
                Toast.makeText(TravelForm.this, "Successfully filled form", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}



