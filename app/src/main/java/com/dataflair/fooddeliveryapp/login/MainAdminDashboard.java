package com.dataflair.fooddeliveryapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.ActivityMainAdminDashboardBinding;
import com.dataflair.fooddeliveryapp.mainadmin.MainAdminHomeFragment;
import com.dataflair.fooddeliveryapp.mainadmin.MainAdminRestaurantListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainAdminDashboard extends AppCompatActivity {

    BottomNavigationView mainAdminBottomNavigationView;
    ActivityMainAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addSuitableFragment(new MainAdminHomeFragment());

        mainAdminBottomNavigationView = findViewById(R.id.main_admin_dashboard_navigation_view);
        binding.mainAdminDashboardNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.home_main_admin_dashboard:
                        addSuitableFragment(new MainAdminHomeFragment());
                        break;
                    case R.id.restaurant_main_admin_dashboard:
                        addSuitableFragment(new MainAdminRestaurantListFragment());
                        break;
                }
                return true;
            }
        });


    }

    private void addSuitableFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_admin_dashboard_layout,fragment);
        fragmentTransaction.commit();
    }

}