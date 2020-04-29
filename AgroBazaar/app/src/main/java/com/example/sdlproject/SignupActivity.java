package com.example.sdlproject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {


    private EditText user_edit, pass_edit, repass_edit, name, phone;
    private TextView state, city, pincode, address, login;
    private Button signup;
    FirebaseAuth firebaseAuth;
    RadioGroup radioGroup;
    RadioButton radioButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteref;
    public double latitude, longitude;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE2 = 102;
    private ProgressDialog pd;
    Geocoder geocoder;
    List<Address> addresses;
    private int radioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user_edit = (EditText) findViewById(R.id.s_username);
        pass_edit = (EditText) findViewById(R.id.s_password);
        repass_edit = (EditText) findViewById(R.id.s_repassword);
        signup = (Button) findViewById(R.id.signup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        name = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone_no);
        address = findViewById(R.id.address);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        pincode = findViewById(R.id.pincode);
        login = findViewById(R.id.login);

        pd = new ProgressDialog(SignupActivity.this);
        pd.setMessage("Registering...");
        pd.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        noteref = db.collection("Users");

        geocoder = new Geocoder(this, Locale.getDefault());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MapsActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                startActivityForResult(i, REQUEST_CODE2);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                final String email = user_edit.getText().toString();
                final String username_st = name.getText().toString();
                final String phone_st = phone.getText().toString();
                final String state_st = state.getText().toString();
                final String city_st = city.getText().toString();
                final String pin_st = pincode.getText().toString();
                final String address_st = address.getText().toString();
                final String type;
//
                try {
                    if (radioButton.getText().equals("Buyer"))
                        type = "Buyer";
                    else
                        type = "Farmer";
                    boolean flag = validation();
                    if (flag == true) {
                        final String finalType = type;
                        firebaseAuth.createUserWithEmailAndPassword(user_edit.getText().toString(),
                                pass_edit.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            users ob = new users(email, type, username_st, phone_st, address_st,
                                                    state_st, city_st, pin_st, latitude, longitude);

                                            noteref.document(FirebaseAuth.getInstance().getCurrentUser()
                                                    .getUid()).set(ob).addOnSuccessListener(
                                                    new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            sendVerificationEmail();
                                                            pd.dismiss();
//
                                                        }
                                                    }
                                            ).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                                                            .show();
                                                    pd.dismiss();
                                                }
                                            });
                                        }

                                    }


                                });
                    }
                    else {
                        pd.dismiss();
                    }
                } catch (NullPointerException e) {

                    Toast.makeText(SignupActivity.this, "Please select type of user", Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                }
            }
        });
    }

    private void sendVerificationEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignupActivity.this, "Registered Successfully. Please check your email for verification.", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });

    }

    public void checkbutton(View view) {

        radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

//        Toast.makeText(SignupActivity.this, "Selected user is" + radioButton.getText(), Toast.LENGTH_SHORT).show();

    }

    private boolean validation() {
        boolean isValid = true;
        if (user_edit.getText().toString().isEmpty()) {
            user_edit.setError("Username Cannot be Empty!");
            isValid = false;
        }

        if (pass_edit.getText().toString().isEmpty()) {
            pass_edit.setError("Password cannot be Empty!");
            isValid = false;
        }

        if (!pass_edit.getText().toString().equals(repass_edit.getText().toString())) {
            isValid = false;
            repass_edit.setError("Password not matched");
        }

        if (phone.getText().length() != 10) {
            isValid = false;
            phone.setError("Phone number should be 10 digits");
        }

        if (name.getText().toString().isEmpty()) {
            name.setError("Field cannot be empty");
            isValid = false;
        }


        return isValid;
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(MapsActivity.this);
                    geocode();
                }
            }
        });
    }

    private void geocode() {
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address.setText(addresses.get(0).getAddressLine(0));
            city.setText(addresses.get(0).getLocality());
            state.setText(addresses.get(0).getAdminArea());
//                        String country = addresses.get(0).getCountryName();
            pincode.setText(addresses.get(0).getPostalCode());

//                        String fullAddress = address + ", " + area + ", " + city + ", " + country + ", "+postalcode;
//                        textView.setText(fullAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE2):
                if (resultCode == RESULT_OK) {
                    latitude = Double.valueOf(data.getStringExtra("latitude"));
                    longitude = Double.valueOf(data.getStringExtra("longitude"));
                    geocode();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
}
