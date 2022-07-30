package com.dataflair.fooddeliveryapp.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dataflair.fooddeliveryapp.Activities.LoginActivity;
import com.dataflair.fooddeliveryapp.Activities.UserRoleActivity;
import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.MainActivity;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentUserLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Executor;


public class UserLoginFragment extends Fragment implements View.OnClickListener{

    private final String TAG= this.getClass().getSimpleName();
    private FragmentUserLoginBinding binding;
    private GoogleSignInClient mSignInClient;
    private FirebaseAuth firebaseAuth;
    private Context mContext;
    private ProgressDialog progressBar;
    private TextInputLayout emailInputTxt,passwordInputTxt;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false);
        View view =inflater.inflate(R.layout.fragment_user_login, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        mContext = view.getContext();

        //Google Signin Options to get gmail and performa gmail login
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("319466083877-4cdnl01r8q8p84nqgtcp42tv8cn0dq12.apps.googleusercontent.com")
                .requestIdToken("729827808042-oq43ack007tr9fqaekunlhp5vqnrpskm.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(view.getContext(), googleSignInOptions);


        progressBar = new ProgressDialog(mContext);
        progressBar.setTitle("Please Wait...");
        progressBar.setMessage("We are setting Everything for you...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        view.findViewById(R.id.iv_google_sign_in).setOnClickListener(this);
        view.findViewById(R.id.tv_forget_password_customer).setOnClickListener(this);
        view.findViewById(R.id.sign_up_customer).setOnClickListener(this);
        view.findViewById(R.id.user_sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.tv_forget_password_customer).setOnClickListener(this);

        emailInputTxt = view.findViewById(R.id.login_mobile_number);
        passwordInputTxt = view.findViewById(R.id.login_password);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference().child("users");

                                    //Hashmap to store the userdetails and setting it to fireabse
                                    HashMap<String, Object> user_details = new HashMap<>();

                                    //Accessing the user details from gmail
                                    String name = googleSignInAccount.getDisplayName().toString();
                                    String mail = googleSignInAccount.getEmail().toString();
                                    String pic = googleSignInAccount.getPhotoUrl().toString();

                                    //user_details.put("id", id);
                                    user_details.put(FDConstants.DISPLAY_NAME, name);
                                    user_details.put(FDConstants.EMAIL, mail);
                                    user_details.put(FDConstants.DISPLAY_PICTURE, pic);
                                    user_details.put(FDConstants.ROLE, "empty");

                                    //Getting the userid of the user from gMail to store the user details under this id
                                    //String userId = GoogleSignIn.getLastSignedInAccount(mContext).getId();

                                    String emailAsFirebaseKey = mail.substring(0,mail.lastIndexOf('@'));


                                    //updating the user details in firebase
                                    myRef.child(emailAsFirebaseKey).updateChildren(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("users")
                                                        .child(emailAsFirebaseKey)
                                                        .child("role")
                                                        .setValue("user").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                                    startActivity(intent);

                                                                }
                                                            }
                                                        });
                                            }
                                            progressBar.cancel();
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
        switch (item){
            case R.id.user_sign_in_button:
                progressBar.show();
                signInWithEmailAndPassword();
                break;
            case R.id.sign_up_customer:
                //Progress bar
                startActivity(new Intent(mContext,LoginActivity.class));
                break;
            case R.id.tv_forget_password_customer:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_login_layout,new UserForgotPasswordFragment())
                                .commit();
                Toast.makeText(mContext,"forgot password text pressed",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_google_sign_in:
                //Showing all Gmails to the user
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
                Log.d(TAG," Inside gmail login button");
                break;



        }
    }

    private void signInWithEmailAndPassword() {
        String tempEmail = emailInputTxt.getEditText().getText().toString();
        String tempPassword = passwordInputTxt.getEditText().getText().toString();
        firebaseAuth.signInWithEmailAndPassword(tempEmail,tempPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.cancel();
                if(task.isSuccessful()){
                    Log.d(TAG," User Login Successful");
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(FDConstants.EMAIL,tempEmail);
                    startActivity(intent);
                    //Getting the userid of the user from gMail to store the user details under this id
                    //String userId = GoogleSignIn.getLastSignedInAccount(mContext).getId();
                    /*String emailAsFirebaseKey = tempEmail.substring(0,tempEmail.lastIndexOf('@'));
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(emailAsFirebaseKey)
                            .child("role")
                            .setValue("user").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        startActivity(intent);

                                    }
                                }
                            });*/
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Login Failed! "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG,"Login Failed! , exception : "+e.getMessage());
            }
        });

    }

   /* private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            //FirebaseUser user = fir.getCurrentUser();
                        } else {
                            // Sign in failed
                        }
                    }
                });
    }*/
}