package com.dataflair.fooddeliveryapp.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentAdminLoginBinding;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;
import com.dataflair.fooddeliveryapp.restaurantadmin.RestaurantAdminDashboard;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class AdminLoginFragment extends Fragment {

    private FragmentAdminLoginBinding binding;
    TextView mainAdminLogin;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    ModelRestaurant modelRestaurant;
    private String restaurantId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAdminLoginBinding.inflate(inflater, container, false);
/*
        View view = inflater.inflate(R.layout.fragment_admin_login,container,false);
*/
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainAdminLogin = view.findViewById(R.id.mainAdministratorLogin);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Verify Restaurant ID...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);


        mainAdminLogin.setOnClickListener(v->{
                MainAdminFragmentLogin adminFragmentLogin = new MainAdminFragmentLogin();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_login_layout,adminFragmentLogin, FDConstants.MAIN_ADMIN_LOGIN)
                        .addToBackStack(null).commit();
        });

        binding.btnAdminContinue.setOnClickListener(v->{
            if(binding.btnAdminContinue.getText() == getString(R.string.login))
            {
                verifyPassword();
            }else{
                progressDialog.show();
                fetchingDataAndVerifyingRestaurantCredentials();
            }

        });
    }

    private void verifyPassword() {
        String tempPassword = binding.restaurantIDLogin.getEditText().getText().toString();
        if(tempPassword.equals(sharedPreferences.getString("Current Restaurant Password",null))){
            Intent intent = new Intent(getContext(), RestaurantAdminDashboard.class);
            intent.putExtra(FDConstants.RESTAURANT_ID,restaurantId);
            startActivity(intent);
        }
    }

    private void fetchingDataAndVerifyingRestaurantCredentials() {
        restaurantId = binding.restaurantIDLogin.getEditText().getText().toString();

        if(restaurantId.isEmpty()){
            Toast.makeText(getContext(),"Restaurant ID is Empty.",Toast.LENGTH_LONG).show();
        }else{
            FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                    .child(restaurantId).child(FDConstants.PERSONAL_DETAILS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()==null){
                                Toast.makeText(getContext(),"Restaurant ID is not valid",Toast.LENGTH_LONG).show();
                            }else{
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                modelRestaurant = snapshot.getValue(ModelRestaurant.class);
                                editor.putString("Current Restaurant Password", modelRestaurant.getRestaurantPassword());
                                editor.commit();

                                MainLogin.currentRestaurantMobileNumber = modelRestaurant.getRestaurantMobileNumber();

                                enterSecretPasswordRestaurant();
                            }
                            progressDialog.cancel();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext()," Verifying Error! Please try again later.",Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                        }
                    });
        }
    }

    private void enterSecretPasswordRestaurant() {
        binding.tvEnterMobileEmailAdmin.setText(R.string.enter_secret_password_description);
        binding.restaurantIDLogin.setHint(R.string.enter_secret_password);
        Objects.requireNonNull(binding.restaurantIDLogin.getEditText()).setText("");
        binding.btnAdminContinue.setText(R.string.login);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}