package com.example.sdlproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment{

    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;
    List<Upload> mUploads ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteref;
    FloatingActionButton fab;
    FirebaseFirestore firestore;
    FirebaseStorage mStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        myrv = (RecyclerView) getActivity().findViewById(R.id.recyclerview_id);

        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mUploads = new ArrayList<>();
        myAdapter = new RecyclerViewAdapter(getActivity(), mUploads);
        mStorage = FirebaseStorage.getInstance();
        noteref = db.collection("CropImage");

        noteref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                mUploads.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Upload upload = documentSnapshot.toObject(Upload.class);
                    mUploads.add(upload);
                }

                myAdapter = new RecyclerViewAdapter(getActivity(), mUploads);
                myrv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                myrv.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();

            }
        });


//        lstBook = new ArrayList<>();
//        lstBook.add(new Crop("Tomato", "Category", "Description", R.drawable.tomato));
//        lstBook.add(new Crop("Rice", "Category", "Description", R.drawable.tomato));
//        lstBook.add(new Crop("Wheat", "Category", "Description", R.drawable.tomato));
//        lstBook.add(new Crop("Corn", "Category", "Description", R.drawable.tomato));
//        lstBook.add(new Crop("Potato", "Category", "Description", R.drawable.potato));
//        lstBook.add(new Crop("Apple", "Category", "Description", R.drawable.potato));
//        lstBook.add(new Crop("Groundnut", "Category", "Description", R.drawable.potato));
//        lstBook.add(new Crop("Cashewnut", "Category", "Description", R.drawable.potato));
//
//        myrv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        myrv.setAdapter(myAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), NewCropActivity.class));
//                myAdapter = new RecyclerViewAdapter(getActivity(), mUploads);
//                myrv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//                myrv.setAdapter(myAdapter);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.crop_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
