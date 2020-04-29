package com.example.sdlproject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private TextView address_update, city_update, pincode_update, state_update;
    private EditText full_name, phone_no;
    private Button updateProfile;
    String n1, c1, s1, a1, p1, pc1, t1;
    double latitude, longitude;
    private ImageView imgView;

    private static final int REQUEST_CODE2 = 102;
    Geocoder geocoder;
    List<Address> addresses;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference noteref = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        full_name = (EditText) getActivity().findViewById(R.id.settings_full_name);
        phone_no = (EditText) getActivity().findViewById(R.id.settings_phone_number);
        address_update = (TextView) getActivity().findViewById(R.id.settings_address);
        city_update = (TextView) getActivity().findViewById(R.id.settings_city);
        state_update = (TextView) getActivity().findViewById(R.id.settings_state);
        pincode_update = (TextView) getActivity().findViewById(R.id.settings_pincode);
        updateProfile = (Button) getActivity().findViewById(R.id.btn_update_profile);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        imgView = getActivity().findViewById(R.id.settings_profile_image);

        noteref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                users ob = documentSnapshot.toObject(users.class);

                n1 = documentSnapshot.getString("name");
                p1 = documentSnapshot.getString("phone");
                s1 = documentSnapshot.getString("state");
                c1 = documentSnapshot.getString("city");
                a1 = documentSnapshot.getString("address");
                pc1 = documentSnapshot.getString("pincode");
                t1 = documentSnapshot.getString("type");
                latitude = documentSnapshot.getDouble("latitude");
                longitude = documentSnapshot.getDouble("longitude");

                if(t1.equals("Buyer"))
                    imgView.setImageResource(R.drawable.boss);
                else
                    imgView.setImageResource(R.drawable.farmer);


                full_name.setText(n1);
                phone_no.setText(p1);
                address_update.setText(a1);
                city_update.setText(c1);
                state_update.setText(s1);
                pincode_update.setText(pc1);
            }
        });

        address_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                startActivityForResult(i, REQUEST_CODE2);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n1 = full_name.getText().toString();
                p1 = phone_no.getText().toString();
                a1 = address_update.getText().toString();
                s1 = state_update.getText().toString();
                c1 = city_update.getText().toString();
                pc1 = pincode_update.getText().toString();
                if(n1.equals(""))
                    Toast.makeText(getActivity(), "Name field cannot be empty", Toast.LENGTH_SHORT).show();
                else if(p1.length() != 10)
                    Toast.makeText(getActivity(), "Phone number should be 10 digits", Toast.LENGTH_SHORT).show();
                else {
                    noteref.update("name", n1);
                    noteref.update("phone", p1);
                    noteref.update("address", a1);
                    noteref.update("city", c1);
                    noteref.update("state", s1);
                    noteref.update("pincode", pc1);
                    noteref.update("latitude", latitude);
                    noteref.update("longitude", longitude);
                    Toast.makeText(getActivity(), "Update Successful", Toast.LENGTH_SHORT).show();
                    if(t1.equals("Buyer"))
                        startActivity(new Intent(getActivity(), HomeActivityBuyer.class));
                    else
                        startActivity(new Intent(getActivity(), HomeActivity.class));


                }
            }
        });
    }

    private void geocode(double latitude, double longitude){
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
            address_update.setText(addresses.get(0).getAddressLine(0));
            city_update.setText(addresses.get(0).getLocality());
            state_update.setText(addresses.get(0).getAdminArea());
//                        String country = addresses.get(0).getCountryName();
            pincode_update.setText(addresses.get(0).getPostalCode());

//                        String fullAddress = address + ", " + area + ", " + city + ", " + country + ", "+postalcode;
//                        textView.setText(fullAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  (REQUEST_CODE2):
                if(resultCode == RESULT_OK){
                    latitude = Double.valueOf(data.getStringExtra("latitude"));
                    longitude = Double.valueOf(data.getStringExtra("longitude"));
                    Log.i("latitude", String.valueOf(latitude));
                    geocode(latitude, longitude);
                }
        }
    }
}
