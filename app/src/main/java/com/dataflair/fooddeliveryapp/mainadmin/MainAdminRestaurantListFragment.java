package com.dataflair.fooddeliveryapp.mainadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class MainAdminRestaurantListFragment extends Fragment {

    private ArrayList<Model> restaurantModelArrayList;
    private HashMap<RestaurantModel,ArrayList<Model>> mixArrayList;
    private RestaurantListAdapter restaurantListAdapter;
    private RecyclerView recyclerView;


    public MainAdminRestaurantListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_admin_restaurant_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantModelArrayList = new ArrayList<>();
        mixArrayList = new HashMap<>();
        recyclerView = view.findViewById(R.id.main_recyclerview);


        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            for(DataSnapshot snapshot : task.getResult().getChildren()){
                                RestaurantModel model = snapshot.child(FDConstants.PERSONAL_DETAILS).getValue(RestaurantModel.class);
                                for(DataSnapshot dataSnapshot : snapshot.child(FDConstants.FOOD_ITEMS).getChildren()){
                                    Model model1 = dataSnapshot.getValue(Model.class);
                                    restaurantModelArrayList.add(model1);
                                }
                                mixArrayList.put(model,restaurantModelArrayList);
                            }
                        }
                    }
                });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                restaurantListAdapter = new RestaurantListAdapter(mixArrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(restaurantListAdapter);
            }
        },1000);


    }
}