package com.dataflair.fooddeliveryapp.restaurantadmin;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Fragments.AddItemFragment;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentRestaurantAdminHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantAdminHomeFragment extends Fragment {

    FragmentRestaurantAdminHomeBinding binding;

    public RestaurantAdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRestaurantAdminHomeBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RestaurantAdminDashboard.isUpdateFoodItem = false;

        binding.addFoodItemRestaurantAdmin.setOnClickListener(v->{
            addFoodItem();
        });

        binding.removeFoodItemRestaurantAdmin.setOnClickListener(v->{
            removeFoodItem();
        });

        showTotalOrdersRestaurant();

    }

    private void showTotalOrdersRestaurant() {
        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                .child(RestaurantAdminDashboard.currentRestaurantId).child(FDConstants.RESTAURANT_ORDERS)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                binding.totalOrderCount.setText(task.getResult().getChildrenCount()+"");
                            }
                    }
                });
    }

    private void removeFoodItem() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.restaurant_admin_dashboard_layout,new RemoveFootItemFragment())
                .addToBackStack(null)
                .commit();
    }

    private void addFoodItem() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.restaurant_admin_dashboard_layout,new AddItemFragment())
                .addToBackStack(FDConstants.RESTAURANT_ADMIN_HOME_FRAGMENT)
                .commit();
    }


}
