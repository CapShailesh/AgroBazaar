package com.example.sdlproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SchemeFragment extends Fragment {

    RecyclerViewAdapterScheme myAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteref = db.collection("Scheme");
    List<Scheme> lstScheme ;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = getActivity().findViewById(R.id.fab);
        final RecyclerView myrv = (RecyclerView) getActivity().findViewById(R.id.recyclerview_id);
        fab.hide();
        lstScheme = new ArrayList<>();



        noteref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots)
                {
                    String name = documentSnapshots.getString("scheme_name");
                    Log.i("CheckDatabase", name);
                    String link = documentSnapshots.getString("link");
                    lstScheme.add(new Scheme(name, link));
                }

                myAdapter = new RecyclerViewAdapterScheme(getActivity(), lstScheme);
                myrv.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                myrv.setAdapter(myAdapter);
            }
        });
    }
}