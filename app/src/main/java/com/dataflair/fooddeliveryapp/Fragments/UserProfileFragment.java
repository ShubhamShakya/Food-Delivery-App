package com.dataflair.fooddeliveryapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.Activities.GetStartedActivity;
import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    CircleImageView circleImageView;
    TextView userNameTxt;
    TextInputLayout emailEditTxt, addressEditTxt, cityNameEditTxt, pinCodeEdittxt,phoneNumberEditTxt,fullNameEditTxt;
    Button signOutBtn, updateDetailsBtn;
    DatabaseReference databaseReference;
    String userId;
    String mEmail;
    String emailAsFirebaseKey;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        //Assigning all the addresses of the android materials
        circleImageView = (CircleImageView) view.findViewById(R.id.ProfileImageView);
        userNameTxt = (TextView) view.findViewById(R.id.UserNameTxt);
        cityNameEditTxt = view.findViewById(R.id.CityEditText);
        emailEditTxt =  view.findViewById(R.id.EmailEditText);
        pinCodeEdittxt =  view.findViewById(R.id.PinCodeExitText);
        addressEditTxt =  view.findViewById(R.id.AddressEditText);
        phoneNumberEditTxt = view.findViewById(R.id.PhoneNumberEditText);
        fullNameEditTxt = view.findViewById(R.id.FullNameEditText);

        updateDetailsBtn = (Button) view.findViewById(R.id.UpdateProfileBtn);
        signOutBtn = (Button) view.findViewById(R.id.SignOutBtn);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        //Getting user detials from GoogleSignin
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        mEmail = getActivity().getIntent().getStringExtra(FDConstants.EMAIL);
        if(mEmail == null && acct!=null){
            mEmail=acct.getEmail();
        }

        if (mEmail != null) {
            //userId = acct.getId().toString();

            emailAsFirebaseKey = mEmail.substring(0,mEmail.lastIndexOf('@'));

            databaseReference.child(emailAsFirebaseKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    //Getting the data from the firebase using model class
                    Model model = snapshot.getValue(Model.class);
                    //setting the data to android materials
                    Picasso.get().load(model.getProfilepic()).into(circleImageView);
                    userNameTxt.setText(model.getDisplayName());
                    Objects.requireNonNull(cityNameEditTxt.getEditText()).setText(model.getCityName());
                    Objects.requireNonNull(emailEditTxt.getEditText()).setText(model.getEmail());
                    Objects.requireNonNull(addressEditTxt.getEditText()).setText(model.getAddress());
                    Objects.requireNonNull(pinCodeEdittxt.getEditText()).setText(model.getPinCode());
                    Objects.requireNonNull(phoneNumberEditTxt.getEditText()).setText(model.getPhoneNumber());
                    Objects.requireNonNull(fullNameEditTxt.getEditText()).setText(model.getDisplayName());

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }


        //Implementing OnClick Listener to update data to firebase
        updateDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting the current data from the edit Text to update it to firebase
                String email = Objects.requireNonNull(emailEditTxt.getEditText()).getText().toString();
                String phoneNumber = Objects.requireNonNull(phoneNumberEditTxt.getEditText()).getText().toString();
                String displayName = Objects.requireNonNull(fullNameEditTxt.getEditText()).getText().toString();

                String cityName = Objects.requireNonNull(cityNameEditTxt.getEditText()).getText().toString();
                String pinCode = Objects.requireNonNull(pinCodeEdittxt.getEditText()).getText().toString();
                String address = Objects.requireNonNull(addressEditTxt.getEditText()).getText().toString();


                //Checking for empty fields
                if (email.isEmpty() || cityName.isEmpty() || pinCode.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || displayName.isEmpty()) {
                    Toast.makeText(getContext(), "Please,Fill Details", Toast.LENGTH_SHORT).show();
                } else {
                    //calling method to update data to firebase
                    updateDetails(email,phoneNumber,displayName, cityName, pinCode, address, userId);
                }
            }
        });

        //implementing onClickListener to make the user signOut
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                //GoogleSignInClient to access the current user
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //User Signout
                            FirebaseAuth.getInstance().signOut();

                            //Redirecting to starting Activity
                            Intent intent = new Intent(getContext(), GetStartedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                });
            }
        });

        return view;
    }

    private void updateDetails(String email,String phoneNumber,String displayName, String cityName, String pinCode, String address, String userId) {


        //Storing the user details in hashmap
        HashMap<String,Object> userDetails = new HashMap();

        //adding the data to hashmap
        userDetails.put(FDConstants.EMAIL, email);
        userDetails.put(FDConstants.CITY, cityName);
        userDetails.put(FDConstants.PIN_CODE, pinCode);
        userDetails.put(FDConstants.ADDRESS, address);
        userDetails.put(FDConstants.MOBILE_NUMBER,phoneNumber);
        userDetails.put(FDConstants.DISPLAY_NAME,displayName);

        //adding the data to firebase
        databaseReference.child(emailAsFirebaseKey).updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {

                if (task.isSuccessful()) {
                    //Showing the Toast message to user
                    Toast.makeText(getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    //Showing the toast message to user
                    Toast.makeText(getContext(), "Please,Try again Later", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}