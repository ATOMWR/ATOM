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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.akav.atom.overtime.AdminOvertimeActivity;
import com.example.akav.atom.travel.AdminTravelActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button checkOvertime;
    private Button checkTravelAllowance;
    private Button verification;

    // String jsonResponse;

    // private final String GET_OT_FORMS_URL = "http://atomwrapp.dx.am/getOtForms.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Intent loginInfo = getIntent();
        String userId = loginInfo.getStringExtra("userId");

        Toast.makeText(this, "Welcome Admin, " + userId, Toast.LENGTH_SHORT).show();

        checkOvertime = (Button) findViewById(R.id.admin_ot);
        checkTravelAllowance = (Button) findViewById((R.id.admin_ta));

        checkOvertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showOvertimeCycles = new Intent(AdminHomeActivity.this, AdminOvertimeActivity.class);
                startActivity(showOvertimeCycles);
            }
        });

        checkTravelAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showTravelCycles = new Intent(AdminHomeActivity.this, AdminTravelActivity.class);
                startActivity(showTravelCycles);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
       // Utils2.setBadgeCount(this, icon, 8);




        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.action_notifications:
                notifications();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void notifications() {

        // Logout Logic
        Intent gotoNotification = new Intent(AdminHomeActivity.this, AdminNotification.class);

        startActivity(gotoNotification);

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
   /* public static class Utils2 {
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
    }*/
}