package com.dataflair.fooddeliveryapp.Activities;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dataflair.fooddeliveryapp.FDBottomSheet;
import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.AddressConfirmBeforeOrderPlaceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class AddressConfirmBottomSheet extends FDBottomSheet<AddressConfirmBeforeOrderPlaceBinding> {

    private Model model, userDataModel;
    private FirebaseAuth mAuth;
    private final static String TAG = AddressConfirmBottomSheet.class.getSimpleName();
    private String mMobileNumber,mFullName,mFullAddress,mCity,mPinCode;

    public AddressConfirmBottomSheet(Model model, Model userDataModel) {
        super();
        this.model = model;
        this.userDataModel = userDataModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.address_confirm_before_order_place;
    }

    @Override
    protected void setupBindingVM() {
        binding = getBinding();
    }

    @Override
    protected void setupUI() {

        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(binding.MobileNumberEditText.getEditText()).setText(userDataModel.getPhoneNumber());
        Objects.requireNonNull(binding.DisplayNameEditText.getEditText()).setText(userDataModel.getDisplayName());
        Objects.requireNonNull(binding.AddressEditText.getEditText()).setText(userDataModel.getAddress());
        Objects.requireNonNull(binding.CityEditText.getEditText()).setText(userDataModel.getCityName());
        Objects.requireNonNull(binding.PinCodeExitText.getEditText()).setText(userDataModel.getPinCode());

        binding.ConfirmOrderButton.setOnClickListener(view -> {
            binding.MobileNumberEditText.setError(null);
            binding.AddressEditText.setError(null);
            binding.PinCodeExitText.setError(null);
            binding.DisplayNameEditText.setError(null);
            binding.CityEditText.setError(null);
            if(verifyingDetails()){
                onConfirmOrder();
            }
        });
    }

    private boolean verifyingDetails() {
        mMobileNumber = Objects.requireNonNull(binding.MobileNumberEditText.getEditText()).getText().toString();
        mFullName = Objects.requireNonNull(binding.DisplayNameEditText.getEditText()).getText().toString();
        mFullAddress = Objects.requireNonNull(binding.AddressEditText.getEditText()).getText().toString();
        mCity = Objects.requireNonNull(binding.CityEditText.getEditText()).getText().toString();
        mPinCode = Objects.requireNonNull(binding.PinCodeExitText.getEditText()).getText().toString();


        return isMobileValidate() && isFullNameValidate() && isFullAddressValidate() && isCityValidate() && isPinCodeValidate();
    }

    private boolean isMobileValidate() {
        if(mMobileNumber.isEmpty()){
            binding.MobileNumberEditText.setError("Can't be empty");
            return false;
        }
        return true;
    }
    private boolean isFullAddressValidate() {
        if(mFullAddress.isEmpty()){
            binding.AddressEditText.setError("Can't be empty");
            return false;
        }
        return true;
    }
 private boolean isCityValidate() {
        if(mCity.isEmpty()){
            binding.CityEditText.setError("Can't be empty");
            return false;
        }
        return true;
    }
 private boolean isPinCodeValidate() {
        if(mPinCode.isEmpty()){
            binding.PinCodeExitText.setError("Can't be empty");
            return false;
        }
        return true;
    }
 private boolean isFullNameValidate() {
        if(mFullName.isEmpty()){
            binding.DisplayNameEditText.setError("Can't be empty");
            return false;
        }
        return true;
    }


    @Override
    protected boolean showFullScreen() {
        return false;
    }


    public void onConfirmOrder() {
        //Generating the unique key
        String key = FirebaseDatabase.getInstance().getReference().child("myOrders").push().getKey().toString();

        //Getting the user email from google sign in
        String firebaseUserEmail = mAuth.getCurrentUser().getEmail();

        //used as a key for database
        String emailAsFirebaseKey = firebaseUserEmail.substring(0, firebaseUserEmail.lastIndexOf('@'));

        //get the updated data
        HashMap<String, Object> orderDetails = getDataWithOrderDetails();
        orderDetails.put(FDConstants.FOOD_ITEM_ORDER_ID,key);

        //Adding the hash map to the database
        FirebaseDatabase.getInstance().getReference().child(FDConstants.MY_ORDERS).child(emailAsFirebaseKey).child(key)
                .setValue(orderDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference().child("totalOrders").child(key)
                                    .setValue(orderDetails)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Showing the toast to user for confirmation
                                                Toast.makeText(getContext(), "Order Confirmed!", Toast.LENGTH_SHORT).show();
                                                getDialog().dismiss();
                                            } else {
                                                getDialog().dismiss();
                                                Toast.makeText(getContext(), "Error in Order Placing! Please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }

                    }
                });

        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT).
                child(binding.MobileNumberEditText.getEditText().getText().toString()).child(FDConstants.RESTAURANT_ORDERS).child(key)
                .setValue(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i(TAG," Order added into the restaurant orders database");
                        }else{
                            Log.e(TAG," Order added failed, error : "+task.getException());
                        }
                    }
                });


    }

    private HashMap<String, Object> getDataWithOrderDetails() {
        //Hash Map to store the values
        HashMap<String, Object> orderDetails = new HashMap();

        //Adding the details to hashmap
        orderDetails.put("name", Objects.requireNonNull(binding.DisplayNameEditText.getEditText()).getText().toString());
        orderDetails.put("phoneNumber", Objects.requireNonNull(binding.MobileNumberEditText.getEditText()).getText().toString());
        orderDetails.put("address", Objects.requireNonNull(binding.AddressEditText.getEditText()).getText().toString());
        orderDetails.put("cityName", Objects.requireNonNull(binding.CityEditText.getEditText()).getText().toString());
        orderDetails.put("pinCode", Objects.requireNonNull(binding.PinCodeExitText.getEditText()).getText().toString());

        orderDetails.put("hotelLocation", model.getHotelLocation());
        orderDetails.put("itemPrice", model.getItemPrice());
        orderDetails.put("itemName", model.getItemName());
        orderDetails.put("imageUrl", model.getImageUrl());
        orderDetails.put(FDConstants.FOOD_ORDER_DELIVERED,false);
        return orderDetails;
    }


}
