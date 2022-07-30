package com.dataflair.fooddeliveryapp.mainadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentGenerateUserIdPasswordForRestaurantAdminBinding;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GenerateUserIdPasswordForRestaurantAdminFragment extends Fragment {

    private FragmentGenerateUserIdPasswordForRestaurantAdminBinding binding;
    private String restaurantId;

    public GenerateUserIdPasswordForRestaurantAdminFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGenerateUserIdPasswordForRestaurantAdminBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.generatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantId = Objects.requireNonNull(binding.restaurantIdPasswordGeneration.getEditText()).getText().toString();
                FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN)
                        .child(FDConstants.RESTAURANT).child(restaurantId).child(FDConstants.PERSONAL_DETAILS)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ModelRestaurant modelRestaurant = snapshot.getValue(ModelRestaurant.class);
                                binding.restaurantConfigurationDetails.setVisibility(View.VISIBLE);
                                binding.restaurantIdTV.setText(restaurantId);
                                if(modelRestaurant!=null){
                                    binding.restaurantPasswordTV.setText(modelRestaurant.getRestaurantPassword());
                                }else{
                                    Toast.makeText(getContext()," Mobile number is not registered",Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(getContext()," Credentials Generated",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext()," Error in generating password, Try again after some time.",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}