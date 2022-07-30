package com.dataflair.fooddeliveryapp.restaurantadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.ActivityRestaurantAdminDashboardBinding;
import com.dataflair.fooddeliveryapp.databinding.FragmentRestaurantAdminHomeBinding;
import com.dataflair.fooddeliveryapp.login.MainLogin;
import com.dataflair.fooddeliveryapp.mainadmin.MainAdminHomeFragment;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class RestaurantAdminDashboard extends AppCompatActivity {

    private BottomNavigationView restaurantAdminBottomNavigationView;
    private ActivityRestaurantAdminDashboardBinding binding;
    private ModelRestaurant modelRestaurant;
    public static String currentRestaurantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addSuitableFragment(new RestaurantAdminHomeFragment());
        currentRestaurantId = getIntent().getStringExtra(FDConstants.RESTAURANT_ID);


        binding.restaurantAdminDashboardNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.RestaurantAdminHomeMenu:
                        addSuitableFragment(new RestaurantAdminHomeFragment());
                        break;
                    case R.id.RestaurantAdminProfileMenu:
                        addSuitableFragment(new RestaurantAdminProfileFragment());
                }
                return true;
            }
        });



    }


    private void addSuitableFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.restaurant_admin_dashboard_layout,fragment);
        fragmentTransaction.commit();
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder logoutAlertDialog = new AlertDialog.Builder(this);
        logoutAlertDialog.setIcon(R.drawable.alert_icon_dialog_box);
        logoutAlertDialog.setMessage("Are you sure, You want to logout.")
                .setTitle("LogOut!");
        logoutAlertDialog.show();

        logoutAlertDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),MainLogin.class));
            }
        }).setNegativeButton("No",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }*/
}