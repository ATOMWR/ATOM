package com.example.akav.atom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akav.atom.overtime.OvertimeActivity;
import com.example.akav.atom.travel.TravelActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class HomeActivity extends AppCompatActivity {

    private Button fillOT;
    private Button fillTA;
    String JSON_STRING,jsonstring,userId;
    TextView temp;
    JSONObject jo;
    JSONArray ja;
    static int globalcount;
    RelativeLayout loadlayout;
    LinearLayout actuallayout;
    int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("home");
        globalcount=0;

        Intent loginInfo = getIntent();
        userId = loginInfo.getStringExtra("userId");
        a=loginInfo.getExtras().getInt("pending");

        Toast.makeText(this, "Welcome " + userId + "!", Toast.LENGTH_SHORT).show();


        fillOT = (Button) findViewById(R.id.fill_ot);
        fillTA = (Button) findViewById(R.id.fill_ta);


        fillOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoOT = new Intent(HomeActivity.this, OvertimeActivity.class);
                gotoOT.putExtra("userID",userId);
                startActivity(gotoOT);
            }
        });

        fillTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoTA = new Intent(HomeActivity.this, TravelActivity.class);
                gotoTA.putExtra("userID",userId);
                startActivity(gotoTA);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // String method = "display";
      //  Backgroundtaskonload backgroundtask2 = new Backgroundtaskonload(this);
       // backgroundtask2.execute(method, userId);

        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        Utils2.setBadgeCount(this, icon, 8);
        // Update LayerDrawable's BadgeDrawable



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.edit_profile:
                editProfile();
                return true;

            case R.id.password_change:
                changePassword();
                return true;
            case R.id.log_out:
                logout();
                return true;

            case R.id.action_notifications:
                notifications();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void notifications() {

        // Logout Logic
        Intent gotoNotification = new Intent(HomeActivity.this, Notification.class);
        gotoNotification.putExtra("userID",userId);
        startActivity(gotoNotification);

    }
private void logout(){

}
    private void changePassword() {

        // Password change logic

    }

    private void editProfile() {

        // Edit Profile Logic

    }

    class Backgroundtaskonload extends AsyncTask<String,Void,String> {

        Context ctx;

        Backgroundtaskonload(Context ctx) {
            this.ctx = ctx;

        }


        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://atomwrapp.dx.am/notificationload.php";
            String method = params[0];
            if (method.equals("display")) {

                String name = params[1];
                // String dat=params[2];


                try {
                    URL url = new URL(reg_url);
                    HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
                    httpurlconnection.setRequestMethod("POST");

                    httpurlconnection.setDoOutput(true);
                    OutputStream OS = httpurlconnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                    String newdata = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");


                    bufferedwriter.write(newdata);
                    // Toast.makeText(ctx, "data written", Toast.LENGTH_LONG).show();

                    bufferedwriter.flush();
                    bufferedwriter.close();
                    OS.close();


                    InputStream inputStream = httpurlconnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {

                        stringBuilder.append(JSON_STRING + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpurlconnection.disconnect();

                    return stringBuilder.toString().trim();


                } catch (MalformedURLException e) {
                    Log.e(MainActivity.class.getName(), "Problem Building the URL", e);
                } catch (IOException e) {
                    Log.e(MainActivity.class.getName(), "Problem in Making HTTP request.", e);
                }
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String res) {
            Toast.makeText(ctx, "ret " + res, Toast.LENGTH_LONG).show();
            jsonstring = res;
            temp = (TextView) findViewById(R.id.tv);
           // temp.setText(res);
            try {
                jo = new JSONObject(jsonstring);
                ja = jo.getJSONArray("countresponse");

                int i = 0;
                int h=0;
                int count = 0;
                while (count < ja.length()) {
                    JSONObject j = ja.getJSONObject(count);
                    h=j.getInt("count");

                    count++;
                }
                String q= String.valueOf(h);
                temp.setText(q);
                HomeActivity.globalcount=h;


                // qr_result.setText(result);
                //jsonstring = res;

                //json();
                // return  result;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static class BadgeDrawable extends Drawable {

        private float mTextSize;
        private Paint mBadgePaint;
        private Paint mTextPaint;
        private Rect mTxtRect = new Rect();

        private String mCount = "";
        private boolean mWillDraw = false;

        public BadgeDrawable(Context context) {
            //mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);
            mTextSize = 18F;

            mBadgePaint = new Paint();
            mBadgePaint.setColor(Color.RED);
            mBadgePaint.setAntiAlias(true);
            mBadgePaint.setStyle(Paint.Style.FILL);

            mTextPaint = new Paint();
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void draw(Canvas canvas) {
            if (!mWillDraw) {
                return;
            }

            Rect bounds = getBounds();
            float width = bounds.right - bounds.left;
            float height = bounds.bottom - bounds.top;

            // Position the badge in the top-right quadrant of the icon.
            float radius = ((Math.min(width, height) / 2) - 1) / 2;
            float centerX = width - radius - 1;
            float centerY = radius + 1;

            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, radius, mBadgePaint);

            // Draw badge count text inside the circle.
            mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
            float textHeight = mTxtRect.bottom - mTxtRect.top;
            float textY = centerY + (textHeight / 2f);
            canvas.drawText(mCount, centerX, textY, mTextPaint);
        }

        /*
        Sets the count (i.e notifications) to display.
         */
        public void setCount(int count) {
            mCount = Integer.toString(count);

            // Only draw a badge if there are notifications.
            mWillDraw = count > 0;
            invalidateSelf();
        }

        @Override
        public void setAlpha(int alpha) {
            // do nothing
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            // do nothing
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }
    public static class Utils2 {
        public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

            HomeActivity.BadgeDrawable badge;

            // Reuse drawable if possible
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
            if (reuse != null && reuse instanceof HomeActivity.BadgeDrawable) {
                badge = (HomeActivity.BadgeDrawable) reuse;
            } else {
                badge = new HomeActivity.BadgeDrawable(context);
            }

            badge.setCount(count);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_badge, badge);
        }


    }

}
