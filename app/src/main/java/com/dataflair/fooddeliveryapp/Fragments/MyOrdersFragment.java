package com.dataflair.fooddeliveryapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.Adapters.MyOrdersAdapter;
import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MyOrdersFragment extends Fragment implements OnMarkDeliveredListener {

    private MyOrdersAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private String emailAsFirebaseKey;


    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);



        mAuth = FirebaseAuth.getInstance();

        //Assigning the Recyclerview to display all ordered  food items
        recyclerView = (RecyclerView) view.findViewById(R.id.MyOrdersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //Fetching details of current login user
        String firebaseUserEmail = mAuth.getCurrentUser().getEmail();

        emailAsFirebaseKey = firebaseUserEmail.substring(0,firebaseUserEmail.lastIndexOf('@'));


        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(FDConstants.MY_ORDERS).child(emailAsFirebaseKey), Model.class)
                        .build();

        adapter = new MyOrdersAdapter(options,this);

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

    @Override
    public void markAsDelivered(String foodOrderItemID) {
            FirebaseDatabase.getInstance().getReference().child(FDConstants.MY_ORDERS).child(emailAsFirebaseKey).child(foodOrderItemID)
                    .child(FDConstants.FOOD_ORDER_DELIVERED).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), R.string.food_delivered_successfully , Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(),"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }

    @Override
    public void onLongClick(String foodItemOrderId) {

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        materialAlertDialogBuilder.setIcon(R.drawable.alert_icon_dialog_box).setTitle(R.string.permanently_remove).setMessage(R.string.permanently_remove_message)
                        .setCancelable(true).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child(FDConstants.MY_ORDERS).child(emailAsFirebaseKey).child(foodItemOrderId)
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(),R.string.food_order_removed,Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),R.string.error+" "+e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           AlertDialog alertDialog = materialAlertDialogBuilder.create();
                           alertDialog.dismiss();
                    }
                }).show();





    }
}