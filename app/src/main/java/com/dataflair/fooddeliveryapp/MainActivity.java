package com.dataflair.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.dataflair.fooddeliveryapp.Activities.AdminActivity;
import com.dataflair.fooddeliveryapp.Activities.GetStartedActivity;
import com.dataflair.fooddeliveryapp.Activities.UserRoleActivity;
import com.dataflair.fooddeliveryapp.Fragments.HomeFragment;
import com.dataflair.fooddeliveryapp.Fragments.MyOrdersFragment;
import com.dataflair.fooddeliveryapp.Fragments.UserProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Assigning framelayout resource file to show appropriate fragment using address
        frameLayout = (FrameLayout) findViewById(R.id.UserFragmentContainer);
        //Assigining Bottomnavigaiton Menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.UserBottomNavigationBar);
        Menu menuNav = bottomNavigationView.getMenu();
        //Setting the default fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, new HomeFragment()).commit();
        //Calling the bottoNavigationMethod when we click on any menu item
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                    //Assigning Fragment as Null
                    Fragment fragment = null;
                    switch (item.getItemId()) {

                        //Shows the Appropriate Fragment by using id as address
                        case R.id.HomeMenu:
                            fragment = new HomeFragment();
                            break;
                        case R.id.MyOrdersMenu:
                            fragment = new MyOrdersFragment();
                            break;

                        case R.id.ProfileMenu:
                            fragment = new UserProfileFragment();
                            break;


                    }
                    //Sets the selected Fragment into the Framelayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, fragment).commit();
                    return true;
                }
            };


    @Override
    protected void onStart() {
        super.onStart();
        //checking user already logged or not
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            Intent intent = new Intent(getApplicationContext(), GetStartedActivity.class);
            startActivity(intent);
            finish();
        } else {
            //Checks for user Role and starts the appropriate activity
           // String id = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();
            String tempEmail = mUser.getEmail();
            String emailAsFirebaseKey = tempEmail.substring(0,tempEmail.lastIndexOf('@'));
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(emailAsFirebaseKey).child("role");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.getValue().toString() != null) {
                        String data = snapshot.getValue().toString();

                        if (data.equals("admin")) {
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (data.equals("empty")) {
                            Intent intent = new Intent(getApplicationContext(), UserRoleActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            //do nothing
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}