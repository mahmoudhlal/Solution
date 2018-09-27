package com.hlal.m7moud.mysolutiontask.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hlal.m7moud.mysolutiontask.Adapters.OrderAdapter;
import com.hlal.m7moud.mysolutiontask.Data.DataItem;
import com.hlal.m7moud.mysolutiontask.MainActivity;
import com.hlal.m7moud.mysolutiontask.R;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment {

    View view ;
    RecyclerView mRecyclerView ;
    OrderAdapter mOrderAdapter ;
    List<DataItem> mUploads ;
    Fragment fragment  ;
    MainActivity mainActivity ;
    FirebaseDatabase mFirebaseDatabase ;
    DatabaseReference mDataRef ;

    FloatingActionButton mAdd ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragement_orders , container , false) ;
        initComponent();

        return view ;
    }

    private void initComponent(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDataRef = mFirebaseDatabase.getReference().child("order");
        RecyclerMain();
        mainActivity = (MainActivity) getActivity();
         fragment  = this ;
        mUploads  = new ArrayList<>();
        mAdd = view.findViewById(R.id.addOrder);
        mOrderAdapter = new OrderAdapter( mUploads , getActivity() , mainActivity );
        mRecyclerView.setAdapter(mOrderAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        dataBaseListener();
        mOrderAdapter.notifyDataSetChanged();

        OnClick();

    }

    // on floating button click
    private void OnClick(){
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container ,
                        new HomeFragment()).commit() ;
            }
        });
    }

    // initialize recycler view
    private void RecyclerMain(){
        mRecyclerView = view.findViewById(R.id.RecView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity()) ;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    //get data from firebase
    private void dataBaseListener(){
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              //  mUploads.clear();

                DataItem dataItem = dataSnapshot.getValue(DataItem.class);

                mUploads.add(dataItem);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
