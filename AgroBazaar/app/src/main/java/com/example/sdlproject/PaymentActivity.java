package com.example.sdlproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    EditText amountEt, noteEt, nameEt, upiIdEt;
    Button sendUpi, debitcard;
    TextView amtToPay, qty_buy, price_buy, trans_price;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id, uid, phone, price, qty, username, name, upi;
    int qtyreq;
    double total_amount;
    double transportCost;

    final int UPI_PAYMENT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        price = intent.getExtras().getString("price");
        qty = intent.getExtras().getString("qty");
        id = intent.getExtras().getString("id");
        uid = intent.getExtras().getString("uid");
        phone = intent.getExtras().getString("phone");
        qtyreq = intent.getExtras().getInt("qtyreq");
        username = intent.getExtras().getString("username");
        name = intent.getExtras().getString("name");
        upi = intent.getExtras().getString("upi");
        transportCost = intent.getExtras().getDouble("transportCost");

        initializeViews();

        try {
            total_amount = Double.valueOf(price) * qtyreq + transportCost;
            amtToPay.setText(String.valueOf(total_amount));
            qty_buy.setText(String.valueOf(qtyreq));
            price_buy.setText(price);
            trans_price.setText(String.valueOf(transportCost));

        } catch (NullPointerException ex) {

        }
        //TODO Display everything required in the ivoice(Prashant)


        sendUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the values from the EditTexts
//                String amount = amountEt.getText().toString();
//                String note = noteEt.getText().toString();
//                String name = nameEt.getText().toString();
//                String upiId = upiIdEt.getText().toString();
                String amount = String.valueOf(total_amount);
                String note = "Check";
                String name = username;
                String upiId = upi;
                payUsingUpi(amount, upiId, name, note);
            }
        });

        debitcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellerTable("10000000");
                addbuyer("10000000");
                deleteSeller();
                sendMessage();
                Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PaymentActivity.this, HomeActivityBuyer.class));
                finish();
            }
        });
    }

    void initializeViews() {
        sendUpi = findViewById(R.id.upi);
        debitcard = (Button) findViewById(R.id.debitcard);
        amtToPay = findViewById(R.id.amount_to_pay);
        qty_buy = findViewById(R.id.quantity_buy);
        price_buy = findViewById(R.id.price_buy);
        trans_price = findViewById(R.id.total_price);
    }

    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
                /*
                Todo (This is Payment Activity) - Shailesh
                 Add in the Ordered List(new collection to be created) of Buyer
                 Also approvalRefNo, save that in OrderedList
                 Remove from the product list(that is product no longer exist)
                 Add in the farmer sell list(new collection to be created)
                 Redirect to home page
                 Finish the activity, make sure back button doesnot bring back to the payment page.(Sailesh)
                 */

                addbuyer(approvalRefNo);
                sellerTable(approvalRefNo);
                deleteSeller();
                sendMessage();
                Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PaymentActivity.this, HomeActivityBuyer.class));
                finish();


            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PaymentActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage() {

        final CollectionReference noteref = db.collection("Message");
        final String[] ph1 = new String[1];
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference note = db.collection("Users").document(id);
        note.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ph1[0] = documentSnapshot.getString("phone");
                PhoneNumber phoneNumber = new PhoneNumber(ph1[0], phone);
                noteref.add(phoneNumber).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PaymentActivity.this, ph1[0], Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void deleteSeller() {

        final DocumentReference noteref = db.collection("Products").document(id);
        noteref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String qty = documentSnapshot.getString("quantity");
//                Log.i("Check", qty);
                //Toast.makeText(PaymentActivity.this, qty, Toast.LENGTH_SHORT).show();
                try {
                    int finalqty = Integer.valueOf(qty) - Integer.valueOf(qtyreq);

                    if (finalqty == 0) {
                        noteref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(PaymentActivity.this, "Success delete", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        noteref.update("quantity", String.valueOf(finalqty)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(PaymentActivity.this, "Success update", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (NullPointerException ex) {

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sellerTable(String approvalRefNo) {

        CollectionReference noteref = db.collection("SoldItem");
        Sold sold = new Sold(approvalRefNo, uid, name, String.valueOf(qtyreq), price);
        noteref.document(sold.getProductRandomKey()).set(sold).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addbuyer(String approvalRefNo) {

        CollectionReference noteref = db.collection("BuyerOrders");
        Bought bought = new Bought(approvalRefNo, FirebaseAuth.getInstance().getCurrentUser().getUid(), name, String.valueOf(qtyreq), price);
        noteref.document(bought.getProductRandomKey()).set(bought).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public class PhoneNumber {
        String phone1, phone2;

        public PhoneNumber(String phone1, String phone2) {
            this.phone1 = phone1;
            this.phone2 = phone2;
        }

        public String getPhone1() {
            return phone1;
        }

        public void setPhone1(String phone1) {
            this.phone1 = phone1;
        }

        public String getPhone2() {
            return phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }
    }
}


