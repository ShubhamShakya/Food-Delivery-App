package com.dataflair.fooddeliveryapp.mainadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentMainAdminHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class MainAdminHomeFragment extends Fragment {

   private TextView tvAddRestaurant,tvDeleteRestaurant;
   private FragmentMainAdminHomeBinding binding;

    public MainAdminHomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainAdminHomeBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAddRestaurant = view.findViewById(R.id.add_restaurant_main_admin);
        tvDeleteRestaurant = view.findViewById(R.id.delete_restaurant_main_admin);

        tvAddRestaurant.setOnClickListener(v->{
            openAddRestaurantScreen();
        });

        binding.generateRestaurantCredentialsMainAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGenerateCredentialsScreen();
            }
        });

        binding.deleteRestaurantMainAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestaurantListScreen();
            }
        });

        displayTotalOrdersCount();

    }

    private void displayTotalOrdersCount() {
        FirebaseDatabase.getInstance().getReference().child("totalOrders").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                binding.totalOrderCount.setText(task.getResult().getChildrenCount()+"");
            }
        });
    }

    private void openRestaurantListScreen() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_dashboard_layout,new DeleteRestaurantMainAdmin())
                .addToBackStack(null).commit();
    }

    private void openGenerateCredentialsScreen() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_dashboard_layout,new GenerateUserIdPasswordForRestaurantAdminFragment())
                .addToBackStack(null).commit();
    }

    private void openAddRestaurantScreen() {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_dashboard_layout,new AddRestaurantFrament())
                    .addToBackStack(null).commit();
    }
}