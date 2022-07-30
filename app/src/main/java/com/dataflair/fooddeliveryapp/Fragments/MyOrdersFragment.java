package com.dataflair.fooddeliveryapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflair.fooddeliveryapp.Adapters.HomeAdapter;
import com.dataflair.fooddeliveryapp.Adapters.MyOrdersAdapter;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MyOrdersFragment extends Fragment {

    MyOrdersAdapter adapter;
    RecyclerView recyclerView;
    String userEmail;
    FirebaseAuth mAuth;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        mAuth = FirebaseAuth.getInstance();

        //Assigning the Recyclerview to display all ordered  food items
        recyclerView = (RecyclerView) view.findViewById(R.id.MyOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Fetching details of current login user
        String firebaseUserEmail = mAuth.getCurrentUser().getEmail();

        String emailAsFirebaseKey = firebaseUserEmail.substring(0,firebaseUserEmail.lastIndexOf('@'));


        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myOrders").child(emailAsFirebaseKey), Model.class)
                        .build();

        adapter = new MyOrdersAdapter(options);

        //setting the adapter to the recyclerview
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //To start listening for the data form the firebase
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        //To stop listening for the data from teh firebase
        adapter.stopListening();
    }
}