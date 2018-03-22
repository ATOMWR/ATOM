package com.example.akav.atom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.scheme.VCard;

import java.io.File;
import java.io.FileOutputStream;

import static com.example.akav.atom.R.id.imageView;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText uniquepfno;

    private Spinner categorySpinner;

    private Button gotoLogin;
    private Button registerButton;

    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private EditText password;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullNameEditText = (EditText) findViewById(R.id.full_name);
        emailEditText = (EditText) findViewById(R.id.email_address);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        registerButton = (Button) findViewById(R.id.qrcodebutton);
        uniquepfno = (EditText) findViewById(R.id.pf_number);
        password=(EditText)findViewById(R.id.password);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Intent gotoHomeIntent = new Intent(RegistrationActivity.this, HomeActivity.class);

                //Only for Testing
                fullName = fullNameEditText.getText().toString();
                Uri nameUri = Uri.parse(fullName);
                gotoHomeIntent.setData(nameUri);
                startActivity(gotoHomeIntent);*/
                alert();
            }
        });

        setupSpinner();

    }

    /**
     * Setup the dropdown spinner that allows the user to select the Category.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(genderSpinnerAdapter);

        // Set the category String based on spinner selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Select Category";
            }
        });
    }

    public void alert() {

        AlertDialog.Builder alertadd = new AlertDialog.Builder(
                RegistrationActivity.this);
        alertadd.setTitle("QR Code verification");


        LayoutInflater factory = LayoutInflater.from(RegistrationActivity.this);
        final View view = factory.inflate(R.layout.qrcode, null);

        // ImageView image= (ImageView) view.findViewById(R.id.imageView);
        // image.setImageResource(R.drawable.logo);
        VCard abhay = new VCard(fullNameEditText.getText().toString())
                .setEmail(password.getText().toString())
                .setTitle( uniquepfno.getText().toString())
                .setPhoneNumber(category)
                .setAddress(category);

        Bitmap myBitmap = QRCode.from(abhay).bitmap();
        final ImageView image = (ImageView) view.findViewById(imageView);
        image.setImageBitmap(myBitmap);


        alertadd.setView(view);
        alertadd.setNeutralButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                BitmapDrawable draw = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
              //  File sdCard = Environment.getDataDirectory();
                File dir = new File("/storage/emulated/0/Atom");
                dir.mkdirs();
                String fileName = String.format("qrcode.jpg", System.currentTimeMillis());

                File outFile = new File(dir, fileName);

                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(outFile));
                sendBroadcast(intent);
                savemessage();
                finish();

            }

        });
        alertadd.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        alertadd.show();

    }

    private void savemessage() {
        Toast.makeText(this, "QRcode saved for verification in future", Toast.LENGTH_SHORT).show();
    }
}



