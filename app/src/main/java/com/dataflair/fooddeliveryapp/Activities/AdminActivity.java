package com.dataflair.fooddeliveryapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.dataflair.fooddeliveryapp.Fragments.AddItemFragment;
import com.dataflair.fooddeliveryapp.Fragments.AdminOrdersFragment;
import com.dataflair.fooddeliveryapp.Fragments.HomeFragment;
import com.dataflair.fooddeliveryapp.Fragments.MyOrdersFragment;
import com.dataflair.fooddeliveryapp.Fragments.UserProfileFragment;
import com.dataflair.fooddeliveryapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Assigning framelayout resource file to show appropriate fragment using address
        frameLayout = (FrameLayout) findViewById(R.id.AdminFragmentContainer);
        //Assigining Bottomnavigaiton Menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.AdminBottomNavigation);
        Menu menuNav = bottomNavigationView.getMenu();
        //Setting the default fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.AdminFragmentContainer, new AddItemFragment()).commit();
        //Calling the bottoNavigationMethod when we click on any menu item
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                    //Assigining Fragment as Null
                    Fragment fragment = null;
                    switch (item.getItemId()) {

                        //Shows the Appropriate Fragment by using id as address
                        case R.id.HomeMenu:
                            fragment = new AddItemFragment();
                            break;
                        case R.id.OrdersMenu:
                            fragment = new AdminOrdersFragment();
                            break;
                        case R.id.ProfileMenu:
                            fragment = new UserProfileFragment();
                            break;

                    }
                    //Sets the selected Fragment into the Framelayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.AdminFragmentContainer, fragment).commit();
                    return true;
                }
            };

}