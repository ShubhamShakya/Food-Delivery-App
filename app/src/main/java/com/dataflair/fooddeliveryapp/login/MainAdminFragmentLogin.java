package com.dataflair.fooddeliveryapp.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentAdminLoginBinding;
import com.dataflair.fooddeliveryapp.databinding.FragmentMainAdminLoginBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class MainAdminFragmentLogin extends Fragment {

    private final String LogInID="205262";
    private final String secretPassword="205262";
    private String mLogInID,mSecretPassword;
    private FragmentMainAdminLoginBinding binding;
    private int wrongCredentialsCount=0;
    private TextInputLayout mTVLogInID,mTVSecretPassword;

    public MainAdminFragmentLogin() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainAdminLoginBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_main_admin_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTVLogInID = view.findViewById(R.id.main_admin_login_id);
        mTVSecretPassword = view.findViewById(R.id.main_admin_secret_password);



        getActivity().findViewById(R.id.main_admin_login_button).setOnClickListener(v->{
            mLogInID = Objects.requireNonNull(mTVLogInID.getEditText()).getText().toString();
            mSecretPassword = Objects.requireNonNull(mTVSecretPassword.getEditText()).getText().toString();
            if(binding.mainAdminLoginButton.isClickable()){
                checkForMainAdminLoginCredentials();
            }else{
                Toast.makeText(getContext(),"LogIn Freeze! Try after One Minute",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void checkForMainAdminLoginCredentials(){
        if(LogInID.equals(mLogInID) && secretPassword.equals(mSecretPassword)){
            startActivity(new Intent(getContext(),MainAdminDashboard.class));
            getActivity().finish();
        }else if(wrongCredentialsCount >= 3){
            binding.mainAdminLoginButton.setClickable(false);
            Toast.makeText(getContext(),"Limit Exceeds! Try after One Minute.",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.mainAdminLoginButton.setClickable(true);
                }
            },30*1000);

        }
        else if(wrongCredentialsCount == 1){
            Toast.makeText(getContext(),"Login Credentials are not correct.Contact to the owner, If you think this was a mistake",Toast.LENGTH_LONG).show();
            wrongCredentialsCount++;
        }
        else{
            wrongCredentialsCount++;
            Toast.makeText(getContext()," Login Credentials are not correct ",Toast.LENGTH_LONG).show();
        }
    }



}