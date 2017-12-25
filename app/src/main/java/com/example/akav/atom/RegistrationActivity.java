package com.example.akav.atom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;

    private Spinner categorySpinner;

    private Button gotoLogin;
    private Button registerButton;

    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullNameEditText = (EditText) findViewById(R.id.full_name);
        emailEditText = (EditText) findViewById(R.id.email_address);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_number);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        gotoLogin = (Button) findViewById(R.id.goto_login_button);
        registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoHomeIntent = new Intent(RegistrationActivity.this, HomeActivity.class);

                //Only for Testing
                fullName = fullNameEditText.getText().toString();
                Uri nameUri = Uri.parse(fullName);
                gotoHomeIntent.setData(nameUri);
                startActivity(gotoHomeIntent);
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoLoginIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(gotoLoginIntent);
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

        // Set the integer mSelected to the constant values
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
}
