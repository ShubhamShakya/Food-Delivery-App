package com.dataflair.fooddeliveryapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflair.fooddeliveryapp.Adapters.AdminOrdersAdapter;
import com.dataflair.fooddeliveryapp.Adapters.MyOrdersAdapter;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.FirebaseDatabase;


public class AdminOrdersFragment extends Fragment {

    AdminOrdersAdapter adapter;
    RecyclerView recyclerView;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminOrdersFragment() {
        // Required empty public constructor
    }

    public static AdminOrdersFragment newInstance(String param1, String param2) {
        AdminOrdersFragment fragment = new AdminOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);

        //Assigning the Recyclerview to display all the orders
        recyclerView = (RecyclerView) view.findViewById(R.id.AdminOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String userId = GoogleSignIn.getLastSignedInAccount(getContext()).getId();

        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("totalOrders"), Model.class)
                        .build();

        adapter = new AdminOrdersAdapter(options);

        //setting the adapter to the recyclerview
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //To start listening for the data from the database
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //To stop the recycler view after closing this fragment
        adapter.stopListening();
    }
}