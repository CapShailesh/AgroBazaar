package com.example.sdlproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FarmersListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private FarmerDisplayAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private FirebaseFirestore db;
    private DocumentReference dr;
    private CollectionReference noteref, noteref2;
    private String receivedProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_list);

        db = FirebaseFirestore.getInstance();
        noteref = db.collection("Products");
        noteref2 = db.collection("Users");
        //TODO Display list of all farmers which have placd request to sell the product mentioned by buyer

        final ArrayList<FarmerItem> farmerList = new ArrayList<>();
        final ArrayList<FarmerItem2> farmerList2 = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerViewFarmer);

        final Intent intent = getIntent();
        receivedProductName = intent.getExtras().getString("Crop_name");

        noteref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            int cnt;

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                user_name = "abc";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("prod_name");
                    if (name.equals(receivedProductName)) {
                        cnt++;
                        String category = documentSnapshot.getString("prod_category");
                        String price = documentSnapshot.getString("price");
                        String date = documentSnapshot.getString("saveCurrentDate");
                        String time = documentSnapshot.getString("saveCurrentTime");
                        String uid = documentSnapshot.getString("uid");
                        String qty = documentSnapshot.getString("quantity");
                        String id = documentSnapshot.getId();
                        String description = documentSnapshot.getString("description");
                        String fertilizer = documentSnapshot.getString("fertilizer");
                        String upi = documentSnapshot.getString("upi");

//                        dr = db.collection("Users").document(uid);
//                        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot2) {
//                                latitude = documentSnapshot2.getDouble("latitude");
//                                longitude = documentSnapshot2.getDouble("longitude");
//                                String user_name = documentSnapshot2.getString("name");
//                                String address = documentSnapshot2.getString("address");
//                                fun(user_name, address);
//                                //Log.i("CheckUID", user_name);
//                            }
//
//
//                        });
                        //Log.i("ChechUID", US());
                        //Log.i("Info" , user_name);
                        farmerList.add(new FarmerItem(name, category, price, date, time, uid, qty, id, description, fertilizer, upi));

                    }
                }

                noteref2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {
                        for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots2) {
                            for (int i = 0; i < cnt; i++) {
                                final FarmerItem currentItem = farmerList.get(i);
                                String uid = currentItem.getUid();
                                if (documentSnapshot2.getId().equals(uid)) {
                                    Double latitude = documentSnapshot2.getDouble("latitude");
                                    Double longitude = documentSnapshot2.getDouble("longitude");
                                    String user_name = documentSnapshot2.getString("name");
                                    String address = documentSnapshot2.getString("address");
                                    String phone = documentSnapshot2.getString("phone");
//                                    Log.i("CheckUID", user_name);
//                                    Log.i("CheckUID", currentItem.getPrice());
                                    farmerList2.add(new FarmerItem2(currentItem.getName(), currentItem.getCategory(), currentItem.getPrice(), currentItem.getDate(), currentItem.getTime(),
                                            currentItem.getUid(), currentItem.getQty(), currentItem.getId(), currentItem.getDescription(), currentItem.getFertilizer(),
                                            user_name, address, latitude, longitude, phone, 0, currentItem.getUpi()));
                                }
                            }
                        }
                        mRecyclerView.setHasFixedSize(true);
                        mlayoutManager = new LinearLayoutManager(FarmersListActivity.this);
                        mAdapter = new FarmerDisplayAdapter(farmerList2);
                        mRecyclerView.setLayoutManager(mlayoutManager);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new FarmerDisplayAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
//                                startActivity(new)
//                                Toast.makeText(FarmersListActivity.this, "Item click at " + position, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(FarmersListActivity.this, farmerList2.get(position).getCategory(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FarmersListActivity.this, ConfirmationActivity.class);
                                intent.putExtra("name", farmerList2.get(position).getName());
                                intent.putExtra("category", farmerList2.get(position).getCategory());
                                intent.putExtra("price", farmerList2.get(position).getPrice());
                                intent.putExtra("date", farmerList2.get(position).getDate());
                                intent.putExtra("uid", farmerList2.get(position).getUid());
                                intent.putExtra("qty", farmerList2.get(position).getQty());
                                intent.putExtra("id", farmerList2.get(position).getId());
                                intent.putExtra("description", farmerList2.get(position).getDescription());
                                intent.putExtra("fertilizer", farmerList2.get(position).getFertilizer());
                                intent.putExtra("username", farmerList2.get(position).getUser_name());
                                intent.putExtra("address", farmerList2.get(position).getAddress());
                                intent.putExtra("phone", farmerList2.get(position).getPhone());
                                intent.putExtra("dist", farmerList2.get(position).getDist());
                                intent.putExtra("upi", farmerList2.get(position).getUpi());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}

