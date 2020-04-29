package com.example.sdlproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminSendMessage extends AppCompatActivity {

    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteref = db.collection("Message");
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_send_message);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminSendMessage.this, "Logout", Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();
                mAuth.signOut();
                finishAffinity();
                startActivity(new Intent(AdminSendMessage.this, MainActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteref.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    return;
                }

                for(DocumentChange dc: queryDocumentSnapshots.getDocumentChanges()){
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    String id = documentSnapshot.getId();
                    String ph1 = documentSnapshot.getString("phone1");
                    String ph2 = documentSnapshot.getString("phone2");

                    switch ((dc.getType())){
                        case ADDED: {
                            int permissionCheck = ContextCompat.checkSelfPermission(AdminSendMessage.this, Manifest.permission.SEND_SMS);

                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                                MyMessage(ph1, ph2);
                                noteref.document(id).delete();

                            } else {

                                ActivityCompat.requestPermissions(AdminSendMessage.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                            }
                        }
                        break;

                    }
                }

            }
        });
    }

    private void MyMessage(String phone1, String phone2) {

        String phoneNumber = phone2;
        String Message = "This message is from Agro Bazaar. This is to inform you that your order has been sold successfully.".trim();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

        phoneNumber = phone1;
        Message = "This message is from Agro Bazaar. This is to inform you that your purchase is successful and it will be delivered to you soon.".trim();

        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
    }
}