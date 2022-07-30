package com.dataflair.fooddeliveryapp.login;

import android.os.Bundle;
import android.view.MenuItem;

import com.dataflair.fooddeliveryapp.databinding.ActivityMainLoginBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dataflair.fooddeliveryapp.R;
import com.google.android.material.navigation.NavigationBarView;

public class MainLogin extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationViewLogin;
    private ActivityMainLoginBinding binding;
    public static String currentRestaurantMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initially user login fragment will be shown
        addSuitableFragment(new UserLoginFragment());

        mBottomNavigationViewLogin = findViewById(R.id.main_login_navigation_view);
        binding.mainLoginNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_login_customer:
                        addSuitableFragment(new UserLoginFragment());
                        break;
                    case R.id.main_login_admin:
                        addSuitableFragment(new AdminLoginFragment());
                        break;
                }


                return true;
            }
        });
    }

    private void addSuitableFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_login_layout,fragment);
        fragmentTransaction.commit();
    }



}