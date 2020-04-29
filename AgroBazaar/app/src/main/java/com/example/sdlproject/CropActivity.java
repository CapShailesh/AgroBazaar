package com.example.sdlproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CropActivity extends AppCompatActivity {

    private TextView tvtitle, tvdescription, tvcategory;
    private ImageView img;
    private Button submit;
    private String Price, Quantity, Description, Fertilizer, Title, category, UPI;
    Button AddNewProductButton;
    private EditText InputProductQuantity, InputProductPrice, InputProuductFertizer, InputProductDescription, InputUpiId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteref = db.collection("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        tvtitle = (TextView) findViewById(R.id.cropTitle);
        tvcategory = (TextView) findViewById(R.id.cropCat);
//        submit = (Button) findViewById(R.id.submit_form);

        //Recieve Data

//        update = findViewById(R.id.upd);
//        Display = findViewById(R.id.display);
        AddNewProductButton =  findViewById(R.id.submit_form);
        InputProductPrice = (EditText) findViewById(R.id.buyer_price_edit);
        InputProductQuantity = (EditText) findViewById(R.id.buyer_qty_edit);
        InputProuductFertizer = (EditText) findViewById(R.id.fertilizer_used_edit);
        InputProductDescription = (EditText) findViewById(R.id.description_edit_text);
        InputUpiId = (EditText) findViewById(R.id.upi_id_edit);

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductData();
            }
        });

        Intent intent = getIntent();
        Title  = intent.getExtras().getString("UploadTitle");
        category = intent.getExtras().getString("Category");

        tvtitle.setText(Title);
        //tvdescription.setText(Description);
        tvcategory.setText(category);

    }

    private void ValidateProductData() {
        Price = InputProductPrice.getText().toString();
        Quantity = InputProductQuantity.getText().toString();
        Description = InputProductDescription.getText().toString();
        Fertilizer = InputProuductFertizer.getText().toString();
        UPI = InputUpiId.getText().toString();

        if (TextUtils.isEmpty(Fertilizer)) {
            Toast.makeText(this, "Please write fertilizer used...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Quantity)) {
            Toast.makeText(this, "Please write product Quantity...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write description of crop...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(UPI)){
            Toast.makeText(this, "Please provide UPI for payment...", Toast.LENGTH_SHORT).show();
        }
        else {
            SaveProductInfoToDatabase();
        }
    }

    private void SaveProductInfoToDatabase() {

        Products products = new Products(
                Fertilizer, Price, Quantity, Description, FirebaseAuth.getInstance().getCurrentUser()
                .getUid(), Title, category, UPI
        );

        noteref.document(products.getProductRandomKey()).set(products).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CropActivity.this, "Success", Toast.LENGTH_SHORT )
                                .show();
                        finish();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CropActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}


