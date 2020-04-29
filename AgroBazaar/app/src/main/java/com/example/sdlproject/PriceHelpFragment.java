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
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class PriceHelpFragment extends Fragment {

    private boolean table_flg=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_price_help, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TableLayout table= getActivity().findViewById(R.id.table);
        final Button switchBtn= getActivity().findViewById(R.id.switchBtn);

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.setColumnCollapsed(1, table_flg);
                table.setColumnCollapsed(2,table_flg);

                if (table_flg)
                {
                    //close
                    table_flg=false;
                    switchBtn.setText("Show Details");
                }
                else
                {
                    //open
                    table_flg=true;
                    switchBtn.setText("Hide Details");
                }
            }
        });

    }
}
