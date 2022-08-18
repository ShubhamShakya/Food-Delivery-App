package com.dataflair.fooddeliveryapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.login.MainLogin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = this.getClass().getSimpleName();
    private GoogleSignInClient mSignInClient;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressBar;
    private Button signInButton;
    private TextInputLayout emailEditTxt, addressEditTxt, cityNameEditTxt, pinCodeEdittxt,passwordEdtTxt,confirmPasswordEdtTxt;
    private TextInputLayout mobileNumberTxt,displayNameTxt;
    private String email,cityName,address,pinCode,passwordString,confirmPasswordString,mobileNumber,displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

       /* //Progress bar
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Please Wait...");
        progressBar.setMessage("We are setting Everything for you...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/


        //Assigning the address of the android materials
        cityNameEditTxt =  findViewById(R.id.CityEditText);
        emailEditTxt =  findViewById(R.id.EmailEditText);
        pinCodeEdittxt =  findViewById(R.id.PinCodeExitText);
        addressEditTxt =  findViewById(R.id.AddressEditText);
        passwordEdtTxt = findViewById(R.id.passwordEditText);
        confirmPasswordEdtTxt = findViewById(R.id.confirmPasswordEditText);
        mobileNumberTxt = findViewById(R.id.MobileNumberEditText);
        displayNameTxt = findViewById(R.id.DisplayNameEditText);
        signInButton = (Button) findViewById(R.id.GoogleSignInBtn);




        //Implementing OnClickListener to perform Login action
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting user details from the edit text
                 email = Objects.requireNonNull(emailEditTxt.getEditText()).getText().toString();
                 cityName = Objects.requireNonNull(cityNameEditTxt.getEditText()).getText().toString();
                 pinCode = Objects.requireNonNull(pinCodeEdittxt.getEditText()).getText().toString();
                 address = Objects.requireNonNull(addressEditTxt.getEditText()).getText().toString();
                 mobileNumber = Objects.requireNonNull(mobileNumberTxt.getEditText()).getText().toString();
                 displayName = Objects.requireNonNull(displayNameTxt.getEditText()).getText().toString();
                 passwordString = Objects.requireNonNull(passwordEdtTxt.getEditText()).getText().toString();
                 confirmPasswordString = Objects.requireNonNull(confirmPasswordEdtTxt.getEditText()).getText().toString();


                //Checking all the fields are filled or not
                if (email.isEmpty() || cityName.isEmpty() || pinCode.isEmpty() || address.isEmpty() || passwordString.isEmpty() || confirmPasswordString.isEmpty()|| mobileNumber.isEmpty() || displayName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please,Fill Details", Toast.LENGTH_SHORT).show();
                } else if(!passwordString.equals(confirmPasswordString)){
                    Toast.makeText(getApplicationContext()," Password must be same",Toast.LENGTH_LONG).show();
                    confirmPasswordEdtTxt.getEditText().setText("");
                }else{
                    createAccountEmailAndPassword();
                }

            }
        });



    }

    private void createAccountEmailAndPassword() {
        firebaseAuth.createUserWithEmailAndPassword(email,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_LONG).show();
                    saveUserDataToFirebaseDatabase();
                    startActivity(new Intent(getApplicationContext(), MainLogin.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Registration Failed! Please Try again. ", Toast.LENGTH_LONG).show();
                    Log.d(TAG,"Registration Failed! , exception : "+task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Registration Error! Please Try after some time. ", Toast.LENGTH_LONG).show();
                Log.e(TAG,"Registration error, exception : "+e.getMessage());
            }
        });
    }

    private void saveUserDataToFirebaseDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");

        //String id = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();//We can't find id here.

        //Hashmap to store the userdetails and setting it to fireabse
        HashMap<String, Object> user_details = new HashMap<>();

        //user_details.put(FDConstants.ID,id);
        user_details.put(FDConstants.EMAIL,email);
        user_details.put(FDConstants.CITY,cityName);
        user_details.put(FDConstants.ADDRESS,address);
        user_details.put(FDConstants.MOBILE_NUMBER,mobileNumber);
        user_details.put(FDConstants.DISPLAY_NAME,displayName);
        user_details.put(FDConstants.PIN_CODE,pinCode);
        user_details.put(FDConstants.DISPLAY_PICTURE,"");
        user_details.put(FDConstants.ROLE,"");

        String emailAsFirebaseKey = email.substring(0,email.lastIndexOf('@'));


        myRef.child(emailAsFirebaseKey).updateChildren(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FirebaseDatabase.getInstance().getReference().child("users").child(emailAsFirebaseKey)
                                .child("role").setValue("user").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG," User data added to database successfully.");
                                        }else{
                                            Log.e(TAG,"Failed to save data to database");
                                        }
                                    }
                                });
                    }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            if (googleSignInAccountTask.isSuccessful()) {
                progressBar.show();
                try {
                    GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(), null);

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference().child("users");

                                    //Hashmap to store the userdetails and setting it to fireabse
                                    HashMap<String, Object> user_details = new HashMap<>();

                                    //Accessing the user details from gmail
                                    //String id = googleSignInAccount.getId().toString();
                                    String name = googleSignInAccount.getDisplayName().toString();
                                    String mail = googleSignInAccount.getEmail().toString();
                                    String pic = googleSignInAccount.getPhotoUrl().toString();


                                    //user_details.put("id", id);
                                    user_details.put("name", name);
                                    user_details.put("mail", mail);
                                    user_details.put("profilepic", pic);
                                    user_details.put("role", "empty");
                                    //user_details.put("phoneNumber", phoneNumber);
                                    user_details.put("cityName", cityName);
                                    user_details.put("pinCode", pinCode);
                                    user_details.put("address", address);

                                    String emailAsFirebaseKey = email.substring(0,email.lastIndexOf('@'));

                                    //updating the user details in firebase
                                    myRef.child(emailAsFirebaseKey).updateChildren(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.cancel();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        int item = v.getId();

    }
}
