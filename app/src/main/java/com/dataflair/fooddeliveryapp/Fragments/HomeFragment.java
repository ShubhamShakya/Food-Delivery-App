package com.dataflair.fooddeliveryapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.Activities.AddressConfirmBottomSheet;
import com.dataflair.fooddeliveryapp.Adapters.HomeAdapter;
import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.OnConfirmOrderListener;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private HomeAdapter adapter;
    private RecyclerView recyclerView;
    private OnConfirmOrderListener onCancelOrderListner;
    FirebaseAuth mAuth;
    ProgressDialog progressBarDialog;
    final String TAG = this.getClass().getSimpleName();
    private ArrayList<Model> modelArrayList,modelRestaurantArrayList;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        modelArrayList = new ArrayList<>();
        modelRestaurantArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        progressBarDialog = new ProgressDialog(getContext());
        progressBarDialog.setMessage("PleaseWait...");


        onCancelOrderListner = new OnConfirmOrderListener() {
            @Override
            public void onConfirmOrder(Model model) {
                progressBarDialog.show();
                showBottomSheetAddressConfirm(model);
            }

            @Override
            public void onConfirmRestaurantDetails(ModelRestaurant modelRestaurant) {

            }
        };


        //Assigning the Recyclerview to display all food items
        recyclerView = (RecyclerView) view.findViewById(R.id.HomeRecyclerView);


        //Firebase Recycler Options to get the data form firebase database using model class and reference
       /* FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT), Model.class)
                        .build();*/



        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isComplete()) {

                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                RestaurantModel model2 = snapshot.child(FDConstants.PERSONAL_DETAILS).getValue(RestaurantModel.class);
                                for(DataSnapshot snapshot1 : snapshot.child(FDConstants.FOOD_ITEMS).getChildren()){
                                    Model model = snapshot1.getValue(Model.class);
                                    model.setHotelLocation(model2.getRestaurantAddress());
                                    model.setRestaurantName(model2.getRestaurantName());
                                    modelArrayList.add(model);
                                }

                            }
                        }
                    }
                });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new HomeAdapter(modelArrayList,onCancelOrderListner);
                //setting the adapter to the recyclerview
                recyclerView.setAdapter(adapter);
            }
        },1000);


        return view;
    }

    private void showBottomSheetAddressConfirm(Model model) {
        //Fetching details of current login user
        String firebaseUserEmail = mAuth.getCurrentUser().getEmail();

        String emailAsFirebaseKey = firebaseUserEmail.substring(0,firebaseUserEmail.lastIndexOf('@'));


        FirebaseDatabase.getInstance().getReference().child("users").child(emailAsFirebaseKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                         //userDataModel[0] = snapshot.getValue(Model.class);
                        progressBarDialog.cancel();
                        AddressConfirmBottomSheet addressConfirmBottomSheet = new AddressConfirmBottomSheet(model, snapshot.getValue(Model.class));
                        addressConfirmBottomSheet.show(getActivity().getSupportFragmentManager(),"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBarDialog.cancel();
                        Toast.makeText(getContext(),"Error! Please try again!",Toast.LENGTH_LONG).show();
                        Log.e(TAG,"Error! Data fetching error, error: "+error);
                    }
                });



        //onConfirmOrder(model);
    }

   /* @Override
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
    }*/

    public void onConfirmOrder(Model model) {
        //Generating the unique key
        String key = FirebaseDatabase.getInstance().getReference().child("myOrders").push().getKey().toString();

        //Getting the user email from google sign in
        String userEmail = Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(getContext())).getEmail();

        //used as a key for database
        assert userEmail != null;
        String emailAsFirebaseKey = userEmail.substring(0,userEmail.lastIndexOf('@'));



        //Database Path to add the details
        FirebaseDatabase.getInstance().getReference().child("users").child(emailAsFirebaseKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Model model = snapshot.getValue(Model.class);

                        //Hash Map to store the values
                        HashMap<String,String> orderDetails = new HashMap();

                        //Adding the details to hashmap
                        orderDetails.put("name", model.getDisplayName());
                        orderDetails.put("phoneNumber", model.getPhoneNumber());
                        orderDetails.put("address", model.getAddress());
                        orderDetails.put("cityName", model.getCityName());
                        orderDetails.put("pinCode", model.getPinCode());

                        orderDetails.put("hotelLocation", model.getHotelLocation());
                        orderDetails.put("itemPrice", model.getItemPrice());
                        orderDetails.put("itemName", model.getItemName());
                        orderDetails.put("imageUrl", model.getImageUrl());
                        //orderDetails.put("userId", userId);


                        //Adding the hash map to the database
                        FirebaseDatabase.getInstance().getReference().child("myOrders").child(emailAsFirebaseKey).child(key)
                                .setValue(orderDetails)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            FirebaseDatabase.getInstance().getReference().child("totalOrders").child(key)
                                                    .setValue(orderDetails)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                //Showing the toast to user for confirmation
                                                                Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


    }
}
