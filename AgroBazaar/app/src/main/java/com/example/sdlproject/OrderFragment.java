package com.example.sdlproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;

    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteref = db.collection("Products");

    ArrayList<OrderItem> orderList;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }
//
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(mRecyclerView);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                int i = item.getGroupId();
                String id = orderList.get(i).getId();
//                Toast.makeText(getActivity(), "Item Deleted " + i, Toast.LENGTH_SHORT).show();
                noteref.document(id).delete();
                Toast.makeText(getActivity(), "Successful Delete", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
//    @Override
//    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
////        MenuInflater menuInflater = getActivity().getMenuInflater();
////        menuInflater.inflate(R.menu.con);
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
////        Long selectedWordID = info.id;
//        menu.add(Menu.NONE, 0, 0, "Delete");
////        Toast.makeText(getActivity(), String.valueOf(selectedWordID), Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
////        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();
////        long i = info.id;
////        int i = mAdapter.getItemId(info.position);
//        long id = mAdapter.getItemId(info.position);
////
//        Toast.makeText(getActivity(), String.valueOf(id) , Toast.LENGTH_SHORT).show();
//
//        return false;
//
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = getActivity().findViewById(R.id.fab);
        mRecyclerView = getActivity().findViewById(R.id.recyclerview_id);
        fab.hide();
        registerForContextMenu(mRecyclerView);

        orderList = new ArrayList<>();

        noteref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("prod_name");
                    String category = documentSnapshot.getString("prod_category");
                    String price = documentSnapshot.getString("price");
                    String date = documentSnapshot.getString("saveCurrentDate");
                    String uid = documentSnapshot.getString("uid");
                    String qty = documentSnapshot.getString("quantity");
                    String id = documentSnapshot.getId();

                    if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        orderList.add(new OrderItem(name, category, date, qty, price, id));
                }

                mRecyclerView.setHasFixedSize(true);
                mlayoutManager = new LinearLayoutManager(getActivity());
                mAdapter = new OrderAdapter(orderList);

                mRecyclerView.setLayoutManager(mlayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
        });



//        textViewData = (getActivity().findViewById(R.id.textViewOrderDisplay));
//
//        noteref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            String data = "";
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
//                {
//                    String name =  documentSnapshot.getString("name");
//                    String category = documentSnapshot.getString("category");
//                    String price = documentSnapshot.getString("price");
//                    String time = documentSnapshot.getString("saveCurrentTime");
//                    String date = documentSnapshot.getString("saveCurrentDate");
//                    String uid = documentSnapshot.getString("uid");
//                    String id = documentSnapshot.getId();
//
//                    if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                       data += "Name"+ name + "category\n" + category + "Price\n" + price + "Time\n"+ time + "Date\n" +date+"\n";
//
//                    //if(name.equals("Wheat"))
//                    //  data += "Name"+ name + "category\n" + category + "Price\n" + price + "Time\n"+ time + "Date\n" +date+"\n";
//
//                    // noteref.document(id).delete();
//
//                    textViewData.setText(data);
//
//
//                }
//
//
//            }
//        });

    }

    public void removeItem(int position){
        //TODO Write Delete code over here
    }
}

