package com.example.sdlproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ConfirmationActivity extends AppCompatActivity {

    private Button buy_button;
    private TextView tvname, tvcategory, tvprice, tvdate, tvqty, tvdescription, tvfertilizer, tvusername, tvaddress, tvdist;
    private EditText qtyRequired;
    private String qtyreq;
    private double transportCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        /*
        Todo
         This is ConfirmationActivity(To be done by Aniket Pandey) -
         (Only layout is required)
         Basically we have to display every information we have to the buyer
         Will provide a sample of the required.
         Buy button at the bottom which will redirect to the payment page
         */

        buy_button = (Button) findViewById(R.id.confirmBuyer);
        tvname = (TextView) findViewById(R.id.prod_name_confirm);
        tvcategory = (TextView) findViewById(R.id.prod_cat_confirm);
        tvdate = (TextView) findViewById(R.id.date_placed);
        tvprice = (TextView) findViewById(R.id.price_confirm);
        tvqty = (TextView) findViewById(R.id.quantity_available);
        tvdescription = (TextView) findViewById(R.id.prod_description);
        tvfertilizer = (TextView) findViewById(R.id.fertilizer_used);
        tvusername = (TextView) findViewById(R.id.seller_name);
        tvaddress = (TextView) findViewById(R.id.seller_address);
        tvdist = (TextView) findViewById(R.id.distance);
        qtyRequired = (EditText) findViewById(R.id.quantity_requested);

        final String upi, name, category, price, date, time, uid, qty, id, description, fertilizer, username, address, phone;
        final Double latitude, longitude, dist;
        final Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        category = intent.getExtras().getString("category");
        price = intent.getExtras().getString("price");
        date = intent.getExtras().getString("date");
        uid = intent.getExtras().getString("uid");
        qty = intent.getExtras().getString("qty");
        id = intent.getExtras().getString("id");
        description = intent.getExtras().getString("description");
        fertilizer = intent.getExtras().getString("fertilizer");
        username = intent.getExtras().getString("username");
        address = intent.getExtras().getString("address");
        phone = intent.getExtras().getString("phone");
        dist = intent.getExtras().getDouble("dist");
        upi = intent.getExtras().getString("upi");

        tvname.setText(name);
        tvcategory.setText(category);
        tvprice.setText(price);
        tvdate.setText(date);
        tvqty.setText(qty);
        tvdescription.setText(description);
        tvfertilizer.setText(fertilizer);
        tvusername.setText(username);
        tvaddress.setText(address);
        tvdist.setText(dist.toString());

        if(dist<=5)
            transportCost = dist * 2;
        else if(dist<=20)
            transportCost = 5 * 2 + (dist-5) * 1.5;
        else if(dist<=50)
            transportCost = 5 * 2 + 15 * 1.5 + (dist-20) * 0.8;
        else
            transportCost = 5 * 2 + 15 * 1.5 + 30 * 0.8 + (dist-50) * 0.4;


        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    qtyreq = qtyRequired.getText().toString();
                    int i = Integer.parseInt(qtyreq);
                    if (Integer.valueOf(qtyreq) > Integer.valueOf(qty)) {
                        Toast.makeText(ConfirmationActivity.this, "Quantity entered cannot be greater than available", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(ConfirmationActivity.this, PaymentActivity.class);
                        intent.putExtra("price", price);
                        intent.putExtra("qty", qty);
                        intent.putExtra("qtyreq", Integer.valueOf(qtyreq));
                        intent.putExtra("id", id);
                        intent.putExtra("uid", uid);
                        intent.putExtra("phone", phone);
                        intent.putExtra("dist", dist);
                        intent.putExtra("name", name);
                        intent.putExtra("username", username);
                        intent.putExtra("upi", upi);
                        intent.putExtra("transportCost", transportCost);
                        startActivity(intent);
                        finish();
                    }
                }catch(NumberFormatException ex){ // handle your exception
                    Toast.makeText(ConfirmationActivity.this, "Enter a value", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
