package com.dataflair.fooddeliveryapp.mainadmin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.OnConfirmOrderListener;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentAddRestaurantFramentBinding;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.RestaurantDatabase;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.RestaurantRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.AsynchronousByteChannel;
import java.util.List;
import java.util.Random;


public class AddRestaurantFrament extends Fragment {

    private Button addRestaurantBtn;
    //private RestaurantModel mRestaurantModel;
    private FragmentAddRestaurantFramentBinding binding;
    private OnConfirmOrderListener listener;
    private RestaurantDatabase restaurantDatabase;
    private ModelRestaurant mRestaurantModel;
    private ProgressDialog progressDialog;


    public AddRestaurantFrament() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding = FragmentAddRestaurantFramentBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addRestaurantBtn = view.findViewById(R.id.addRestaurantBtnMainAdmin);
        //mRestaurantModel = new ModelRestaurant();
        restaurantDatabase = RestaurantDatabase.getInstance(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Adding Details...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        listener = new OnConfirmOrderListener() {
            @Override
            public void onConfirmOrder(Model model) {

            }

            @Override
            public void onConfirmRestaurantDetails(ModelRestaurant modelRestaurant) {
                progressDialog.show();

                String restaurantPassword = (new Random().nextInt(999999 - 100000 + 1) + 100000)+"";
                modelRestaurant.setRestaurantPassword(restaurantPassword);

                FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT).child(modelRestaurant.getRestaurantMobileNumber()).child(FDConstants.PERSONAL_DETAILS)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT).child(modelRestaurant.getRestaurantMobileNumber()).child(FDConstants.PERSONAL_DETAILS)
                                                .setValue(modelRestaurant)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(getContext()," Restaurant Added Successfully.",Toast.LENGTH_LONG).show();
                                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_dashboard_layout,new MainAdminHomeFragment())
                                                                        .addToBackStack(null).commit();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext()," Error! not able to add restaurant, "+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                progressDialog.cancel();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                  Toast.makeText(getContext(),"Error! not able to add restaurant, "+error.getDetails(),Toast.LENGTH_LONG).show();
                            }
                        });

            }
        };


        addRestaurantBtn.setOnClickListener(v->{

                    mRestaurantModel = new ModelRestaurant(binding.FullNameRestaurantText.getEditText().getText().toString(),
                    binding.OwnerNameRestaurantEditText.getEditText().getText().toString(),
                    binding.AddressEditText.getEditText().getText().toString(),
                    binding.EmailRestaurantEditText.getEditText().getText().toString(),
                    binding.mobileNumberRestaurantEditText.getEditText().getText().toString(),
                    binding.PinCodeRestaurantExitText.getEditText().getText().toString(),null);

            ConfirmationBottomSheetForAddRestaurant bottomSheetForAddRestaurant = new ConfirmationBottomSheetForAddRestaurant(mRestaurantModel,listener);
            bottomSheetForAddRestaurant.show(getActivity().getSupportFragmentManager(),"");
        });


    }

}