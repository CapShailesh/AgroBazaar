package com.example.sdlproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText user_edit, pass_edit;
    private Button login;
    private TextView register;
    private TextView forgotpass;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser currentUser;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Authenticating...");
        pd.setCancelable(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference noteref = db.collection("Users");
        if (user != null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String rid = currentUser.getUid();

            noteref.document(rid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String type = documentSnapshot.getString("type");
                    String email = documentSnapshot.getString("email");
                    if (email.equals("prashantsa255@gmail.com")){
                        finishAffinity();
                        startActivity(new Intent(MainActivity.this, AdminSendMessage.class));
                    }
                    else if (type.equals("Buyer")) {
                        finishAffinity();
                        startActivity(new Intent(MainActivity.this, HomeActivityBuyer.class));
                    }
                    else {
                        finishAffinity();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                }
            });
        }

        user_edit = (EditText) findViewById(R.id.username);
        pass_edit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        register = (TextView) findViewById(R.id.register);

        forgotpass = (TextView) findViewById(R.id.forgotPassword);


        firebaseAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignupActivity.class));

            }

        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ForgotActivity.class));
                        //TODO Forget password page design -- Completed
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                boolean flag = validation();

                if (flag == true) {
                    firebaseAuth.signInWithEmailAndPassword(user_edit.getText().toString().trim(),
                            pass_edit.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                            String rid = currentUser.getUid();
                                            noteref.document(rid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    String type = documentSnapshot.getString("type");
                                                    String email = documentSnapshot.getString("email");
                                                    if (email.equals("prashantsa255@gmail.com")){
                                                        Toast.makeText(MainActivity.this,"Logged in as Admin", Toast.LENGTH_SHORT).show();
                                                        finishAffinity();
                                                        startActivity(new Intent(MainActivity.this, AdminSendMessage.class));
                                                        pd.dismiss();
                                                    }
                                                    else if (type.equals("Buyer")) {
                                                        Toast.makeText(MainActivity.this, "Logged in Successfully",
                                                                Toast.LENGTH_LONG).show();
                                                        finishAffinity();
                                                        startActivity(new Intent(MainActivity.this, HomeActivityBuyer.class));
                                                        pd.dismiss();
                                                    }
                                                    else {
                                                        Toast.makeText(MainActivity.this, "Logged in Successfully",
                                                                Toast.LENGTH_LONG).show();
                                                        finishAffinity();
                                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                        pd.dismiss();
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(MainActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            pd.dismiss();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }
                                }
                            });
                }
                else {
                    pd.dismiss();
                }
            }
        });

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
        return isValid;
    }
}