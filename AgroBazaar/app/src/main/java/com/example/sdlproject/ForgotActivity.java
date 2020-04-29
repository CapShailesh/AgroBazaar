package com.example.sdlproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    Button Send;
    EditText Enter_email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Send = (Button) findViewById(R.id.send);
        Enter_email = (EditText) findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.sendPasswordResetEmail(Enter_email.getText().toString()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotActivity.this, "Password Reset link send to your mail",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(ForgotActivity.this, MainActivity.class));
                                } else {
                                    Toast.makeText(ForgotActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                );
            }
        });
    }
}
