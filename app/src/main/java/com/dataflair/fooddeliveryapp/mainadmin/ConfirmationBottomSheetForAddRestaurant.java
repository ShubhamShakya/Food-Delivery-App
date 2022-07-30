package com.dataflair.fooddeliveryapp.mainadmin;

import com.dataflair.fooddeliveryapp.FDBottomSheet;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.OnConfirmOrderListener;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.ConfirmationBottomSheetForAddRestaurantBinding;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;

public class ConfirmationBottomSheetForAddRestaurant extends FDBottomSheet<ConfirmationBottomSheetForAddRestaurantBinding> {

    private ModelRestaurant restaurantModel;
    private ConfirmationBottomSheetForAddRestaurantBinding binding;
    private OnConfirmOrderListener listener;


    public ConfirmationBottomSheetForAddRestaurant(ModelRestaurant restaurantModel, OnConfirmOrderListener listener){
        this.restaurantModel = restaurantModel;
        this.listener = listener;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.confirmation_bottom_sheet_for_add_restaurant;
    }

    @Override
    protected void setupBindingVM() {
        binding = getBinding();

    }

    @Override
    protected void setupUI() {
        binding.fullNameRest.setText(restaurantModel.getRestaurantName());
        binding.ownerNameRest.setText(restaurantModel.getRestaurantOwnerName());
        binding.mobileNumberRest.setText(restaurantModel.getRestaurantMobileNumber());
        binding.emailRest.setText(restaurantModel.getRestaurantEmail());
        binding.fullAddressRest.setText(restaurantModel.getRestaurantAddress());
        binding.pinCodeRest.setText(restaurantModel.getRestaurantPinCode());

        binding.editBackButton.setOnClickListener(v->{
            getDialog().dismiss();
        });

        binding.confirmAddRestaurantMainAdmin.setOnClickListener(v->{
            getDialog().dismiss();
            listener.onConfirmRestaurantDetails(restaurantModel);
        });

    }

    @Override
    protected boolean showFullScreen() {
        return false;
    }
}
