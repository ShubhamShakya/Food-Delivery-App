package com.dataflair.fooddeliveryapp.restaurantadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentRestaurantAdminProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RestaurantAdminProfileFragment extends Fragment {

    private FragmentRestaurantAdminProfileBinding binding;


    public RestaurantAdminProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantAdminProfileBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                .child(RestaurantAdminDashboard.currentRestaurantId).child(FDConstants.PERSONAL_DETAILS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RestaurantModel restaurantModel = snapshot.getValue(RestaurantModel.class);
                        binding.restaurantFullNameRestAdmin.setText(restaurantModel.getRestaurantName());
                        binding.restaurantFullNameUpdateRestAdmin.getEditText().setText(restaurantModel.getRestaurantName());
                        binding.restaurantAddressUpdateRestAdmin.getEditText().setText(restaurantModel.getRestaurantAddress());
                        binding.restaurantCityUpdateRestAdmin.getEditText().setText(restaurantModel.getRestaurantOwnerName());
                        binding.restaurantPinCodeUpdateRestAdmin.getEditText().setText(restaurantModel.getRestaurantPinCode());
                        binding.restaurantPhoneNumberUpdateRestAdmin.getEditText().setText(restaurantModel.getRestaurantMobileNumber());
                        binding.restaurantEmailUpdateRestAdmin.getEditText().setText(restaurantModel.getRestaurantEmail());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        binding.UpdateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    RestaurantModel restaurantModel = new RestaurantModel();

                HashMap<String,Object> hashMap = new HashMap<>();

                    hashMap.put(FDConstants.RESTAURANT_EMAIL,binding.restaurantEmailUpdateRestAdmin.getEditText().getText().toString());
                    hashMap.put(FDConstants.RESTAURANT_ADDRESS,binding.restaurantEmailUpdateRestAdmin.getEditText().getText().toString());
                    hashMap.put(FDConstants.RESTAURANT_OWNER_NAME,binding.restaurantCityUpdateRestAdmin.getEditText().getText().toString());
                    hashMap.put(FDConstants.RESTAURANT_PIN_CODE,binding.restaurantPinCodeUpdateRestAdmin.getEditText().getText().toString());
                    hashMap.put(FDConstants.RESTAURANT_MOBILE_NUMBER,binding.restaurantPhoneNumberUpdateRestAdmin.getEditText().getText().toString());
                    hashMap.put(FDConstants.RESTAURANT_FULLNAME,binding.restaurantFullNameUpdateRestAdmin.getEditText().getText().toString());

                    updateRestaurantDetails(hashMap);
            }
        });
    }

    private void updateRestaurantDetails(HashMap<String,Object> hashMap) {

        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                .child(RestaurantAdminDashboard.currentRestaurantId).child(FDConstants.PERSONAL_DETAILS)
                .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Information Updated successfully ", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Error in Updating Restaurant Details ", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}